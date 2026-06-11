import streamlit as st
import requests

# Configuração da página
st.set_page_config(page_title="Comércio Eletrônico", layout="centered")

URL_BASE = "http://localhost:8080"

class ClienteTemplate:

    @staticmethod
    @st.cache_data(ttl=5) # guarda o status na memória para evitar lentidão nos cliques
    def verificar_status_api():
        try:
            return requests.get(f"{URL_BASE}/health", timeout=0.5).status_code
        except Exception:
            return 500
    
    #pag temporarias
    @staticmethod
    def pagina_carrinho():
        st.header("🛒 Meu Carrinho")
        st.info("Página em construção! Aqui você verá os itens que selecionou.")
    @staticmethod
    def pagina_historico():
        st.header("🧾 Histórico de Compras")
        st.info("Página em construção! Suas compras anteriores serão listadas aqui.")
    @staticmethod
    def redirecionar_para_produtos(pg_produtos):
        # Esta função só serve para jogar o usuário para a URL /produtos
        st.switch_page(pg_produtos)

    @staticmethod
    def renderizar_navegacao():

        # página raiz (default) invisível
        pg_produtos = st.Page(
            "template\cliente\catalogo_produtos.py", 
            title="Produtos Disponíveis", 
            icon="📦",
            url_path="produtos" 
        )
        pg_raiz = st.Page(
            ClienteTemplate.redirecionar_para_produtos(pg_produtos), 
            title="Início", 
            default=True 
        )


        pg_carrinho = st.Page(
            ClienteTemplate.pagina_carrinho, 
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


        # NAVEGAÇÃO E SIDEBAR


        menu_navegacao = st.navigation([pg_raiz, pg_produtos,  pg_carrinho, pg_historico], position="hidden")

        with st.sidebar:
            st.header("Seja bem-vindo!")
            
            st.page_link(pg_produtos)
            st.page_link(pg_carrinho)
            st.page_link(pg_historico)
            
            if st.button("Sair"):
                del st.session_state.id_usuario
                st.rerun()

        menu_navegacao.run()