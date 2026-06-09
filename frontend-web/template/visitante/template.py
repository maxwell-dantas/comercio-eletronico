import streamlit as st
import requests

URL_BASE = "http://localhost:8080"

class VisitanteTemplate:

    @staticmethod
    @st.cache_data(ttl=5) # guarda o status na memória para evitar lentidão nos cliques
    def verificar_status_api():
        try:
            return requests.get(f"{URL_BASE}/health", timeout=0.5).status_code
        except Exception:
            return 500

    @staticmethod
    def redirecionar_para_login():
        st.switch_page("template/visitante/login.py")

    @staticmethod
    def renderizar_navegacao():

        # serve para contornar o problema da página default do streamlit que não mostra a URL correta da página
        pg_raiz = st.Page(VisitanteTemplate.redirecionar_para_login, title="Início", default=True)

        pg_login = st.Page("template/visitante/login.py", title="Login", icon="🔑", url_path="login")
        pg_cadastro = st.Page("template/visitante/cadastro.py", title="Cadastro", icon="📝", url_path="cadastro")

        # esconde a navegação padrão do Streamlit
        menu_visitante = st.navigation([pg_raiz, pg_login, pg_cadastro], position="hidden")

        with st.sidebar:
            st.subheader("Status do Sistema")

            if (VisitanteTemplate.verificar_status_api() == 200):
                st.success("🟢 Back-end Online")
            else:
                st.error("🔴 Back-end Offline")

            st.divider()

            # links de navegação
            st.page_link(pg_login)
            st.page_link(pg_cadastro)

        # roda a tela selecionada da navegação
        menu_visitante.run()
