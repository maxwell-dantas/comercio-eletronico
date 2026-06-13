from template.cliente.servico_api import ServicoCarrinhoAPI, ServicoProdutosAPI
import streamlit as st
import time

class CarrinhoView:
    
    @staticmethod
    def renderizar_carrinho():
        st.set_page_config(page_title="Meu Carrinho", layout="centered")

        nome = st.session_state.get("nome_usuario", "Cliente")
        st.header(f"🛒 Carrinho de {nome}")
        st.divider()

        if "id_venda" not in st.session_state:
            st.info("Seu carrinho está vazio ou a sessão expirou.")
            if st.button("Voltar aos Produtos"):
                st.switch_page("template/cliente/catalogo_produtos.py")
            st.stop()

        id_venda_atual = st.session_state["id_venda"]

        # Busca os dados brutos do carrinho E do catálogo de produtos
        itens_carrinho = ServicoCarrinhoAPI.buscar_itens(id_venda_atual)
        produtos_loja = ServicoProdutosAPI.buscar_catalogo()

        if not itens_carrinho:
            st.info("Nenhum produto foi adicionado ainda.")
            if st.button("Ir para Produtos"):
                st.switch_page("template/cliente/catalogo_produtos.py")
        else:
            
            dict_produtos = {p["id"]: p for p in produtos_loja}
            total_geral = 0.0
            
             
            for item in itens_carrinho:
                with st.container(border=True):
                    
                    id_item_carrinho = item.get("id") # ID da linha do carrinho 
                    id_produto = item.get("idProduto") # ID do produto físico 
                    quantidade = item.get("quantidade", 1)
                    
                    
                    produto_detalhes = dict_produtos.get(id_produto, {})
                    nome_produto = produto_detalhes.get("descricao", f"Produto Código #{id_produto}")
                    preco_unitario = produto_detalhes.get("preco", 0.0)
                    subtotal = preco_unitario * quantidade
                    
                    
                    total_geral += subtotal
                    
                    col_info, col_del = st.columns([4, 1], vertical_alignment="center")
                    
                    with col_info:
                        st.markdown(f"**{nome_produto}**")
                        st.caption(f"Qtd: {quantidade} x {preco_unitario:.2f} reais | Subtotal: R$ {subtotal:.2f}")
                        
                    with col_del:
                        if st.button("🗑️", key=f"del_{id_item_carrinho}", help="Remover este item"):
                            status, msg = ServicoCarrinhoAPI.remover_item_especifico(id_venda_atual, id_item_carrinho)
                            
                            if status == 200:
                                st.toast("Item removido!")
                                time.sleep(1)
                                st.rerun()
                            else:
                                st.error(f"Erro: {msg}")
                                
             
            
            st.divider()
            
            
            col_texto, col_valor = st.columns([3, 1])
            with col_valor:
                st.subheader(f"Total: R$ {total_geral:.2f}")
            
            st.write("") 
            
            col1, col2 = st.columns(2)
            
            with col1:
                if st.button("🗑️ Limpar Carrinho", use_container_width=True):
                    status, msg = ServicoCarrinhoAPI.limpar_carrinho(id_venda_atual)
                    if status == 200:
                        st.success("Seu carrinho foi esvaziado!")
                        time.sleep(1.5)
                        st.rerun()
                    else:
                        st.error(f"Erro: {msg}")
                        
            with col2:
                if st.button("✅ Finalizar Compra", type="primary", use_container_width=True):
                    status, msg = ServicoCarrinhoAPI.finalizar_compra(id_venda_atual)
                    
                    if status == 200:
                        st.success("🎉 Compra finalizada com sucesso! Agradecemos a preferência.")
                        st.balloons() 
                        del st.session_state["id_venda"]
                        time.sleep(2.5)
                        st.switch_page("template/cliente/catalogo_produtos.py")
                    else:
                        st.error(f"Erro: {msg}")


CarrinhoView.renderizar_carrinho()