import streamlit as st
import requests
import pandas as pd
from datetime import datetime
import time

st.set_page_config(page_title="Mercadinho Caju - Vendas", page_icon="💰", layout="wide")

URL_BASE = "http://localhost:8080"

st.title("💰 Relatório e Gestão de Vendas")
st.write("Filtre as vendas por período para visualizar os detalhes, itens comprados e alocar entregadores.")

# =========================================================================
# Gerenciamento de Estado da Tela
# =========================================================================
if "gerar_relatorio" not in st.session_state:
    st.session_state.gerar_relatorio = False
if "tipo_busca" not in st.session_state:
    st.session_state.tipo_busca = None
if "data_inicio_busca" not in st.session_state:
    st.session_state.data_inicio_busca = None
if "data_fim_busca" not in st.session_state:
    st.session_state.data_fim_busca = None

with st.container(border=True):
    col1, col2 = st.columns(2)
    with col1:
        data_inicio = st.date_input("Data de Início", format="DD/MM/YYYY")
    with col2:
        data_fim = st.date_input("Data de Fim", format="DD/MM/YYYY")

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

# =========================================================================
# Renderização Independente (Processa a resposta da API)
# =========================================================================
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
            
    else:  # Caso 'todas'
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
    
    resposta_itens = requests.get(f"{URL_BASE}/venda_itens")
    resposta_produtos = requests.get(f"{URL_BASE}/produtos")
    resposta_categorias = requests.get(f"{URL_BASE}/categorias")
    resposta_usuarios = requests.get(f"{URL_BASE}/clientes")
    
    todos_os_itens = resposta_itens.json() if resposta_itens.status_code == 200 else []
    lista_usuarios = resposta_usuarios.json() if resposta_usuarios.status_code == 200 else []
    entregadores = [u for u in lista_usuarios if u.get("idFuncao") == 3]
    
    dict_produtos = {p['id']: p for p in (resposta_produtos.json() if resposta_produtos.status_code == 200 else [])}
    dict_categorias = {c['id']: c for c in (resposta_categorias.json() if resposta_categorias.status_code == 200 else [])}
    dict_entregadores = {e['id']: e['nome'] for e in entregadores}
    dict_clientes = {u['id']: u['nome'] for u in lista_usuarios} # Dicionário para mapear nomes de clientes
    
    total_faturado = sum(v.get('total', 0.0) for v in vendas_filtradas) 
    st.success(f"**Total Faturado:** R$ {total_faturado:.2f}")
    st.markdown("---")

    for venda in vendas_filtradas:
        # Formatação de Data para o Padrão Brasileiro
        try:
            data_obj = datetime.strptime(venda['data'][:16], "%Y-%m-%dT%H:%M")
            data_formatada = data_obj.strftime("%d/%m/%Y %H:%M")
        except:
            data_formatada = "Data Indisponível"
            
        valor_venda = venda.get('total', 0.0) 
        status_atual = venda.get('statusEntrega', 'AGUARDANDO_ENTREGADOR')
        
        # Resgatando o Nome do Cliente
        id_cliente = venda.get('idCliente')
        nome_cliente = dict_clientes.get(id_cliente, f"Desconhecido (ID {id_cliente})")
        
        # Título do Expander melhorado
        titulo_venda = f"Venda #{venda['id']} | Cliente: {nome_cliente} | Data: {data_formatada} | Total: R$ {valor_venda:.2f} | Status: {status_atual}"
        
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
            
            if itens_desta_venda:
                itens_formatados = []
                for item in itens_desta_venda:
                    prod_id = item.get('idProduto')
                    produto = dict_produtos.get(prod_id, {})
                    nome_produto = produto.get('descricao', f"Produto Removido (ID {prod_id})")
                    cat_id = produto.get('idCategoria')
                    nome_categoria = dict_categorias.get(cat_id, {}).get('descricao', "Sem Categoria")
                    
                    itens_formatados.append({
                        "Quantidade": item.get('quantidade'),
                        "Produto": nome_produto,
                        "Categoria": nome_categoria,
                        "Preço Unitário (R$)": f"{item.get('preco', 0.0):.2f}"
                    })
                
                df_itens = pd.DataFrame(itens_formatados)
                st.dataframe(df_itens, use_container_width=True, hide_index=True)
            else:
                st.write("Nenhum item detalhado encontrado nesta venda.")