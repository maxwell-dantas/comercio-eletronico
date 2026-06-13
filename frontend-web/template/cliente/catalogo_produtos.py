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

        # 1. Prepara a sessão do usuário
        CatalogoProdutos.inicializar_carrinho()

        # 2. Busca os dados simultaneamente através da API
        produtos = ServicoProdutosAPI.buscar_catalogo()
        promocoes = ServicoProdutosAPI.buscar_promocao()

        if not produtos: 
            st.stop()

        # 3. Transforma a lista de promoções em um Dicionário Python
        # Isso faz a busca ser instantânea, evitando lentidão na tela!
        dict_promocoes = {promo["idProduto"]: promo for promo in promocoes if "idProduto" in promo}

        for produto in produtos:
            with st.container(border=True):
                col_img, col_info, col_qtd, col_btn = st.columns([2, 4, 2, 1], vertical_alignment="center")
                
                esgotado = produto["estoque"] <= 0 
                id_prod = produto["id"]
                
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
                    
                    # Verifica se o ID deste produto está na lista de promoções
                    if id_prod in dict_promocoes:
                        promo_atual = dict_promocoes[id_prod]
                        
                        # --- CÁLCULO DO DESCONTO TEMPORÁRIO ---
                        percentual = promo_atual.get("percentualDesconto", 10) 
                        preco_com_desconto = preco_original - (preco_original * (percentual / 100))
                        
                        # Mostra o preço original riscado (~~texto~~) e o novo preço
                        st.markdown(f"De: ~~R$ {preco_original:.2f}~~ por **R$ {preco_com_desconto:.2f}** 🏷️")
                    else:
                        # Se não tem promoção, mostra normal
                        st.caption(f"R$ {preco_original:.2f}")
                        
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