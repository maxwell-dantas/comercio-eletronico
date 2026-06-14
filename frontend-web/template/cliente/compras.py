from template.cliente.servico_api import ServicoProdutosAPI, ServicoCarrinhoAPI
from datetime import datetime
import streamlit as st
import base64

class HistoricoCompras:
    @staticmethod
    def renderizar_compras():
        st.set_page_config(page_title="Meus Pedidos - Mercadinho Caju", layout="centered")

        if "id_usuario" not in st.session_state:
            st.error("Você precisa estar logado para acessar suas compras.")
            st.stop()

        nome = st.session_state.get("nome_usuario", "Cliente")
        st.header(f"🧾 Histórico de Pedidos - {nome}")
        st.divider()

        id_cliente = st.session_state["id_usuario"]

        with st.expander("🔎 Filtrar e Buscar Pedidos", expanded=True):
            col_data, col_status = st.columns(2)

            with col_data:
                filtro_datas = st.date_input(
                    "Por período de compra",
                    value=[],
                    max_value=datetime.today().date(),
                    format="DD/MM/YYYY"
                )

            with col_status:
                filtro_status = st.selectbox(
                    "Por status da entrega",
                    ["Todos", "Aguardando", "Em rota", "Entregue"]
                )

        st.write("")

        historico_bruto = ServicoCarrinhoAPI.buscar_historico(id_cliente)
        produtos_loja = ServicoProdutosAPI.buscar_catalogo()

        dict_produtos = {p["id"]: p for p in produtos_loja}
        compras_finalizadas = [venda for venda in historico_bruto if not venda.get("carrinho", True)]

        mapa_status = {
            "Aguardando": "AGUARDANDO_ENTREGADOR",
            "Em rota": "EM_ROTA",
            "Entregue": "ENTREGUE"
        }

        compras_filtradas = []
        for compra in compras_finalizadas:
            incluir_na_lista = True

            if filtro_status != "Todos":
                status_esperado_java = mapa_status.get(filtro_status)
                if compra.get("statusEntrega", "AGUARDANDO_ENTREGADOR") != status_esperado_java:
                    incluir_na_lista = False

            if filtro_datas and incluir_na_lista:
                data_str = compra.get("data")
                if data_str:
                    try:
                        data_compra = datetime.strptime(data_str.split(".")[0], "%Y-%m-%dT%H:%M:%S").date()

                        if len(filtro_datas) == 2:
                            data_inicio, data_fim = filtro_datas
                            if not (data_inicio <= data_compra <= data_fim):
                                incluir_na_lista = False
                        elif len(filtro_datas) == 1:
                            if data_compra != filtro_datas[0]:
                                incluir_na_lista = False
                    except Exception:
                        pass

            if incluir_na_lista:
                compras_filtradas.append(compra)

        # Ordena da data mais recente para a mais antiga
        compras_filtradas.sort(key=lambda x: x.get("data") or "", reverse=True)

        if not compras_filtradas:
            st.info("Nenhum pedido encontrado para os filtros selecionados.")
            st.stop()

        for compra in compras_filtradas:
            id_venda = compra.get("id")
            total = compra.get("total", 0.0)
            status_entrega = compra.get("statusEntrega", "AGUARDANDO_ENTREGADOR")
            id_entregador = compra.get("idEntregador", 0)

            data_str = compra.get("data")
            data_formatada = "Data não registrada"
            if data_str:
                try:
                    d_obj = datetime.strptime(data_str.split(".")[0], "%Y-%m-%dT%H:%M:%S")
                    data_formatada = d_obj.strftime("%d/%m/%Y às %H:%M")
                except Exception:
                    data_formatada = data_str

            data_entrega_str = compra.get("dataEntrega")
            data_entrega_formatada = None
            if data_entrega_str:
                try:
                    de_obj = datetime.strptime(data_entrega_str.split(".")[0], "%Y-%m-%dT%H:%M:%S")
                    data_entrega_formatada = de_obj.strftime("%d/%m/%Y às %H:%M")
                except Exception:
                    data_entrega_formatada = data_entrega_str

            with st.container(border=True):
                c1, c2 = st.columns([3, 1], vertical_alignment="center")

                with c1:
                    st.markdown(f"#### 📦 Pedido #{id_venda}")
                    st.caption(f"🗓️ Feito em: {data_formatada}")

                    if status_entrega == "ENTREGUE":
                        st.success("✅ Entregue com sucesso")
                        if data_entrega_formatada:
                            st.caption(f"🏁 Recebido em: {data_entrega_formatada}")

                    elif status_entrega == "EM_ROTA":
                        st.warning("🏍️ O entregador está a caminho do seu endereço")
                        if id_entregador > 0:
                            st.caption(f"👤 Código do Entregador: {id_entregador}")

                    else:
                        st.info("⏳ Aguardando a alocação de um entregador")

                with c2:
                    st.markdown("<p style='text-align: right; margin:0;'>Total</p>", unsafe_allow_html=True)
                    st.markdown(f"<h3 style='text-align: right; margin:0; color:#2E7D32;'>R$ {total:.2f}</h3>", unsafe_allow_html=True)

                with st.expander("🔍 Ver itens deste pedido"):
                    itens_deste_pedido = ServicoCarrinhoAPI.buscar_itens(id_venda)

                    if not itens_deste_pedido:
                        st.caption("Não foi possível carregar os itens deste pedido.")
                    else:
                        for item in itens_deste_pedido:
                            id_prod = item.get("idProduto")
                            quantidade = item.get("quantidade", 1)

                            detalhes = dict_produtos.get(id_prod, {})
                            nome_prod = detalhes.get("descricao", f"Produto #{id_prod}")
                            img_base64 = detalhes.get("imagemBase64")
                            preco_un = item.get("preco", detalhes.get("preco", 0.0))

                            col_img, col_txt, col_sub = st.columns([1, 4, 2], vertical_alignment="center")

                            with col_img:
                                if img_base64:
                                    try:
                                        st.image(base64.b64decode(img_base64), use_container_width=True)
                                    except Exception:
                                        st.image("https://placehold.co/150x150?text=Erro", use_container_width=True)
                                else:
                                    st.image("https://placehold.co/150x150?text=Sem+Imagem", use_container_width=True)

                            with col_txt:
                                st.markdown(f"**{nome_prod}**")
                                st.caption(f"Qtd: {quantidade} x R$ {preco_un:.2f}")

                            with col_sub:
                                st.markdown(f"<p style='text-align: right; margin:0;'>R$ {(preco_un * quantidade):.2f}</p>", unsafe_allow_html=True)

HistoricoCompras.renderizar_compras()