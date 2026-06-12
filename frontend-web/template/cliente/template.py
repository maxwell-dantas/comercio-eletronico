import streamlit as st
import requests

URL_BASE = "http://localhost:8080"

class ClienteTemplate:

    @staticmethod
    @st.cache_data(ttl=5) 
    def verificar_status_api():
        try:
            return requests.get(f"{URL_BASE}/health", timeout=0.5).status_code
        except Exception:
            return 500

    # PÁGINAS TEMPORÁRIAS E DE REDIRECIONAMENTO
    
    @staticmethod
    def pagina_raiz():
        st.switch_page("template/cliente/catalogo_produtos.py")
      
    @staticmethod
    def pagina_historico():
        st.header("🧾 Histórico de Compras")
        st.info("Página em construção! Suas compras anteriores serão listadas aqui.")
    # RENDERIZAÇÃO DO MENU 
   
    @staticmethod
    def renderizar_navegacao():

        pg_raiz = st.Page(
            ClienteTemplate.pagina_raiz, 
            title="Início", 
            default=True # É a página padrão, mas ninguém fica nela
        )

        # Página de Produtos 
        pg_produtos = st.Page(
            "template/cliente/catalogo_produtos.py", 
            title="Produtos Disponíveis", 
            icon="📦",
            url_path="produtos" # O direcionamento correto que você queria
        )

        # Página de carrinho (construindo)
        pg_carrinho = st.Page(
            "template/cliente/carrinho.py", 
            title="Meu Carrinho", 
            icon="🛒",
            url_path="carrinho"
        )

        pg_historico = st.Page(
            ClienteTemplate.pagina_historico, 
            title="Histórico de Compras", 
            icon="🧾",
            url_path="historico"
        )

        # NAVEGAÇÃO E SIDEBAR (Incluindo a raiz na lista)
        menu_navegacao = st.navigation(
            [pg_raiz, pg_produtos, pg_carrinho, pg_historico], 
            position="hidden"
        )

        with st.sidebar:
            st.header(f"Seja bem-vindo(a), {st.session_state.get('nome_usuario', '')}!")
            st.divider()
            
            # Os botões para o cliente clicar
            st.page_link(pg_produtos)
            st.page_link(pg_carrinho)
            st.page_link(pg_historico)

            if st.button("Sair"):
                del st.session_state.id_usuario
                st.rerun()
        menu_navegacao.run()