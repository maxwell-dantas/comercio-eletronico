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
    
    # PÁGINAS TEMPORÁRIAS E DE REDIRECIONAMENTO 
    
    @staticmethod
    def pagina_raiz():
        
        st.switch_page("template/cliente/catalogo_produtos.py")

    @staticmethod
    def pagina_carrinho():
        st.header("🛒 Meu Carrinho")
        st.info("Página em construção! Aqui você verá os itens que selecionou.")
    
    @staticmethod
    def pagina_historico():
        st.header("🧾 Histórico de Compras")
        st.info("Página em construção! Suas compras anteriores serão listadas aqui.")
    
    # RENDERIZAÇÃO DO MENU

    @staticmethod
    def renderizar_navegacao():

        # Página Raiz Invisível 
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
            url_path="produtos" 
        )

        # páginas
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

        # NAVEGAÇÃO E SIDEBAR (Incluindo a raiz na lista)
        menu_navegacao = st.navigation(
            [pg_raiz, pg_produtos, pg_carrinho, pg_historico], 
            position="hidden"
        )

        with st.sidebar:
            st.header(f"Seja bem-vindo(a), {st.session_state.get('nome_usuario', '')}!")
            
            # Os botões para o cliente clicar
            st.page_link(pg_produtos)
            st.page_link(pg_carrinho)
            st.page_link(pg_historico)

            if st.button("Sair"):
                del st.session_state.id_usuario
                st.rerun()

        menu_navegacao.run()