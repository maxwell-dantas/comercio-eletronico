from template.cliente.servico_api import ServicoProdutosAPI, ServicoCarrinhoAPI
import streamlit as st
import time
import base64

class CatalogoProdutos:

    @staticmethod
    def inicializar_carrinho():
        if "id_usuario" not in st.session_state:
            st.error("Você precisa estar logado para acessar os produtos.")
            st.stop()

        if "id_venda" not in st.session_state:
            id_venda = ServicoCarrinhoAPI.inicializar_carrinho(st.session_state.id_usuario)
            if id_venda:
                st.session_state["id_venda"] = id_venda

    @staticmethod
    def enviar_para_carrinho(produto_id, nome_produto, quantidade_selecionada):
        if "id_venda" not in st.session_state:
            st.error("Erro: Nenhum carrinho ativo encontrado!")
            return

        id_venda_atual = st.session_state["id_venda"]
        status, mensagem = ServicoCarrinhoAPI.adicionar_item(id_venda_atual, produto_id, quantidade_selecionada)

        if status == 201:
            st.success(f"{quantidade_selecionada}x {nome_produto} adicionado(s) ao carrinho!")
        else:
            st.warning(f"Erro: {mensagem}")

    @staticmethod
    def renderizar_catalogo():
        st.header("Veja nossos produtos disponíveis")
        st.divider()

        # Prepara a sessão do usuário
        CatalogoProdutos.inicializar_carrinho()

        # Busca os dados simultaneamente através da API
        produtos = ServicoProdutosAPI.buscar_catalogo()
        promocoes = ServicoProdutosAPI.buscar_promocao()

        if not produtos:
            st.stop()

        # Transforma a lista de promoções em um Dicionário Python
        dict_promocoes = {promo["idCategoria"]: promo for promo in promocoes if "idCategoria" in promo}

        for produto in produtos:
            with st.container(border=True):
                col_img, col_info, col_qtd, col_btn = st.columns([2, 4, 2, 1], vertical_alignment="center")

                esgotado = produto["estoque"] <= 0
                id_prod = produto["id"]
                id_categoria = produto.get("idCategoria")

                with col_img:
                    img_base64 = produto.get("imagemBase64")

                    if img_base64:
                        try:
                            imagem_bytes = base64.b64decode(img_base64)
                            st.image(imagem_bytes, use_container_width=True)
                        except Exception:
                            st.image("https://placehold.co/150x150?text=Erro+na+Imagem", use_container_width=True)
                    else:
                        st.image("https://placehold.co/150x150?text=Sem+Imagem", use_container_width=True)

                with col_info:
                    st.markdown(f"**{produto['descricao']}**")

                    preco_original = produto['preco']

                    if id_categoria in dict_promocoes:
                        promo_atual = dict_promocoes[id_categoria]
                        percentual = promo_atual.get("percentualDesconto", 10)
                        preco_com_desconto = preco_original - (preco_original * (percentual / 100))

                        # FORMATO E-COMMERCE
                        st.markdown(
                            f"<div style='line-height: 1.2; margin-top: 8px;'>"
                            f"<span style='color: #94a3b8; text-decoration: line-through; font-size: 14px;'>R$ {preco_original:.2f}</span><br>"
                            f"<span style='color: #22c55e; font-weight: 800; font-size: 22px;'>R$ {preco_com_desconto:.2f}</span>"
                            f"<span style='background-color: #ef4444; color: white; padding: 2px 6px; border-radius: 4px; font-size: 12px; font-weight: bold; margin-left: 8px; vertical-align: middle;'>-{int(percentual)}% OFF</span>"
                            f"</div>",
                            unsafe_allow_html=True
                        )
                    else:
                        # Preço normal também formatado com destaque
                        st.markdown(
                            f"<div style='line-height: 1.2; margin-top: 8px;'>"
                            f"<span style='font-weight: 800; font-size: 22px;'>R$ {preco_original:.2f}</span>"
                            f"</div>",
                            unsafe_allow_html=True
                        )

                    st.caption(f"Estoque: {produto['estoque']}")

                with col_qtd:
                    qtd = st.number_input(
                        "Quantidade",
                        min_value=0 if esgotado else 1,
                        max_value=produto["estoque"],
                        step=1,
                        key=f"qtd_{id_prod}",
                        disabled=esgotado,
                        label_visibility="collapsed"
                    )

                with col_btn:
                    carrinho = st.button("🛒", key=f"add_{id_prod}", disabled=esgotado, use_container_width=True)

                if carrinho:
                    CatalogoProdutos.enviar_para_carrinho(id_prod, produto["descricao"], qtd)
                    time.sleep(2)
                    st.rerun()

CatalogoProdutos.renderizar_catalogo()