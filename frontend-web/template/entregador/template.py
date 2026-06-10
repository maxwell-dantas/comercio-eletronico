import streamlit as st
import requests

URL_BASE = "http://localhost:8080"

class EntregadorTemplate:

    @staticmethod
    @st.cache_data(ttl=5) # guarda a resposta na memória por 5 segundos
    def verificar_status_api(): # verifica a API a cada 5 segundos
        try:
            return requests.get(f"{URL_BASE}/health", timeout=0.5).status_code
        except Exception:
            return 500

    @staticmethod
    def redireciona_para_pendentes():
        st.switch_page("template/entregador/pendentes.py")

    @staticmethod
    def renderizar_navegacao():

        # serve para contornar o problema da página default do streamlit que não mostra a URL correta da página
        pg_raiz = st.Page(EntregadorTemplate.redireciona_para_pendentes, title="Início", default=True)

        pg_pendentes = st.Page("template/entregador/pendentes.py", title="Entregas Pendentes", icon="📦", url_path="entregador-pendentes")
        pg_historico = st.Page("template/entregador/historico.py", title="Histórico de Entregas", icon="✅", url_path="entregador-historico")

        # esconde a navegação padrão do Streamlit
        menu_entregador = st.navigation([pg_raiz, pg_pendentes, pg_historico], position="hidden")

        with st.sidebar:
            st.subheader("Status do Sistema")

            if (EntregadorTemplate.verificar_status_api() == 200):
                st.success("🟢 Back-end Online")
            else:
                st.error("🔴 Back-end Offline")

            st.divider()

            # links de navegação
            st.page_link(pg_pendentes)
            st.page_link(pg_historico)

            # Botão de sair destacado no final da sidebar
            if (st.button("Sair", icon="🚪")):
                del st.session_state.id_usuario
                del st.session_state.id_funcao
                del st.session_state.nome_usuario
                st.rerun()

        # roda a tela selecionada da navegação
        menu_entregador.run()