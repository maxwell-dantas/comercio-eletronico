import streamlit as st
import requests
import base64
from datetime import datetime
import time

st.set_page_config(page_title="Mercadinho Caju - Vendas", page_icon="💰", layout="wide")

URL_BASE = "http://localhost:8080"

st.title("💰 Relatório e Gestão de Vendas")
st.write("Filtre as vendas por período e status para visualizar detalhes e gerenciar logística.")

if "gerar_relatorio" not in st.session_state:
    st.session_state.gerar_relatorio = False
if "tipo_busca" not in st.session_state:
    st.session_state.tipo_busca = None
if "data_inicio_busca" not in st.session_state:
    st.session_state.data_inicio_busca = None
if "data_fim_busca" not in st.session_state:
    st.session_state.data_fim_busca = None

with st.container(border=True):
    col1, col2, col3 = st.columns(3)
    with col1:
        data_inicio = st.date_input("Data de Início", format="DD/MM/YYYY")
    with col2:
        data_fim = st.date_input("Data de Fim", format="DD/MM/YYYY")
    with col3:
        filtro_status_admin = st.selectbox(
            "Filtrar por Status",
            ["Todos", "Sem Entregador", "Em Rota", "Entregues"]
        )

    col_btn1, col_btn2 = st.columns(2)
    with col_btn1:
        if st.button("Gerar Relatório por Período", use_container_width=True):
            st.session_state.gerar_relatorio = True
            st.session_state.tipo_busca = "periodo"
            st.session_state.data_inicio_busca = data_inicio
            st.session_state.data_fim_busca = data_fim

    with col_btn2:
        if st.button("Ver Todas as Vendas Concluídas", type="primary", use_container_width=True):
            st.session_state.gerar_relatorio = True
            st.session_state.tipo_busca = "todas"

if st.session_state.gerar_relatorio:
    vendas_filtradas = []

    if st.session_state.tipo_busca == "periodo":
        url_relatorio = f"{URL_BASE}/vendas/relatorio?inicio={st.session_state.data_inicio_busca}&fim={st.session_state.data_fim_busca}"
        resposta_vendas = requests.get(url_relatorio)
        resposta_vendas.encoding = "utf-8"

        if resposta_vendas.status_code == 200:
            vendas_filtradas = resposta_vendas.json()
        else:
            st.error(resposta_vendas.text)
            st.session_state.gerar_relatorio = False
            st.stop()

    else:
        resposta_vendas = requests.get(f"{URL_BASE}/vendas")
        resposta_vendas.encoding = "utf-8"

        if resposta_vendas.status_code == 200:
            todas_as_vendas = resposta_vendas.json()
            vendas_filtradas = [v for v in todas_as_vendas if not v.get("carrinho", True)]

            if not vendas_filtradas:
                st.info("Nenhuma venda concluída encontrada no histórico geral.")
                st.stop()
        else:
            st.error(resposta_vendas.text)
            st.session_state.gerar_relatorio = False
            st.stop()

    mapa_status = {
        "Sem Entregador": "AGUARDANDO_ENTREGADOR",
        "Em Rota": "EM_ROTA",
        "Entregues": "ENTREGUE"
    }

    # Aplica o filtro de status da dropdown
    vendas_exibicao = []
    for v in vendas_filtradas:
        if filtro_status_admin != "Todos":
            if v.get('statusEntrega', 'AGUARDANDO_ENTREGADOR') != mapa_status[filtro_status_admin]:
                continue
        vendas_exibicao.append(v)

    # Ordena da venda mais nova para a mais velha
    vendas_exibicao.sort(key=lambda x: x.get('data') or '', reverse=True)

    resposta_itens = requests.get(f"{URL_BASE}/venda_itens")
    resposta_itens.encoding = "utf-8"
    resposta_produtos = requests.get(f"{URL_BASE}/produtos")
    resposta_produtos.encoding = "utf-8"
    resposta_usuarios = requests.get(f"{URL_BASE}/clientes")
    resposta_usuarios.encoding = "utf-8"

    todos_os_itens = resposta_itens.json() if resposta_itens.status_code == 200 else []
    lista_usuarios = resposta_usuarios.json() if resposta_usuarios.status_code == 200 else []
    entregadores = [u for u in lista_usuarios if u.get("idFuncao") == 3]

    dict_produtos = {p['id']: p for p in (resposta_produtos.json() if resposta_produtos.status_code == 200 else [])}
    dict_entregadores = {e['id']: e['nome'] for e in entregadores}
    dict_clientes = {u['id']: u['nome'] for u in lista_usuarios}

    total_faturado = sum(v.get('total', 0.0) for v in vendas_exibicao)
    st.success(f"**Total Faturado (na seleção):** R$ {total_faturado:.2f}")
    st.markdown("---")

    if not vendas_exibicao:
        st.info("Nenhuma venda corresponde aos filtros aplicados.")
        st.stop()

    total_de_vendas = len(vendas_exibicao)

    for indice, venda in enumerate(vendas_exibicao, start=1):
        try:
            data_obj = datetime.strptime(venda['data'][:16], "%Y-%m-%dT%H:%M")
            data_formatada = data_obj.strftime("%d/%m/%Y %H:%M")
        except:
            data_formatada = "Data Indisponível"

        valor_venda = venda.get('total', 0.0)
        status_atual = venda.get('statusEntrega', 'AGUARDANDO_ENTREGADOR')

        id_cliente = venda.get('idCliente')
        nome_cliente = dict_clientes.get(id_cliente, f"Desconhecido (ID {id_cliente})")

        # Criação do Numerador Sequencial Decrescente
        numero_venda = total_de_vendas - indice + 1
        titulo_venda = f"Venda #{numero_venda} | Cliente: {nome_cliente} | Data: {data_formatada} | Total: R$ {valor_venda:.2f} | Status: {status_atual}"

        with st.expander(titulo_venda):
            st.write(f"**Nome do Comprador:** {nome_cliente}")
            st.write("### 🚚 Controle de Logística")

            if status_atual == "AGUARDANDO_ENTREGADOR":
                if not entregadores:
                    st.warning("⚠️ Não há entregadores cadastrados no sistema para alocação.")
                else:
                    entregador_sel = st.selectbox(
                        "Selecione um Entregador Disponível:",
                        options=entregadores,
                        format_func=lambda ent: ent["nome"],
                        key=f"sel_ent_{venda['id']}"
                    )

                    if st.button("Confirmar Alocação", key=f"btn_aloc_{venda['id']}"):
                        url_alocar = f"{URL_BASE}/vendas/{venda['id']}/alocar?idEntregador={entregador_sel['id']}"
                        resposta_alocacao = requests.put(url_alocar)
                        resposta_alocacao.encoding = "utf-8"

                        if resposta_alocacao.status_code == 200:
                            st.success("Pedido alocado e despachado com sucesso!")
                            time.sleep(1.5)
                            st.rerun()
                        else:
                            st.error(resposta_alocacao.text)

            elif status_atual == "EM_ROTA":
                nome_motoca = dict_entregadores.get(venda.get('idEntregador'), "Desconhecido")
                st.warning(f"🛵 O pedido está em rota de entrega com o profissional: **{nome_motoca}**.")

            elif status_atual == "ENTREGUE":
                nome_motoca = dict_entregadores.get(venda.get('idEntregador'), "Desconhecido")
                st.success(f"✅ Entrega concluída com sucesso por: **{nome_motoca}**.")

            st.markdown("---")
            st.write("**Itens da Venda:**")

            itens_desta_venda = [item for item in todos_os_itens if item.get('idVenda') == venda['id']]

            if not itens_desta_venda:
                st.caption("Nenhum item detalhado encontrado nesta venda.")
            else:
                for item in itens_desta_venda:
                    prod_id = item.get('idProduto')
                    quantidade = item.get('quantidade', 1)

                    detalhes = dict_produtos.get(prod_id, {})
                    nome_prod = detalhes.get("descricao", f"Produto #{prod_id}")
                    img_base64 = detalhes.get("imagemBase64")
                    preco_un = item.get("preco", detalhes.get("preco", 0.0))

                    # Proporção ajustada para layout="wide" e imagem fixa
                    col_img, col_txt, col_sub = st.columns([1, 8, 2], vertical_alignment="center")

                    with col_img:
                        if img_base64:
                            try:
                                # Fixado em 100px para não estourar em telas grandes
                                st.image(base64.b64decode(img_base64), width=100)
                            except Exception:
                                st.image("https://placehold.co/100x100?text=Erro", width=100)
                        else:
                            st.image("https://placehold.co/100x100?text=Sem+Imagem", width=100)

                    with col_txt:
                        st.markdown(f"**{nome_prod}**")
                        st.caption(f"Qtd: {quantidade} x R$ {preco_un:.2f}")

                    with col_sub:
                        st.markdown(f"<p style='text-align: right; margin:0;'>R$ {(preco_un * quantidade):.2f}</p>", unsafe_allow_html=True)