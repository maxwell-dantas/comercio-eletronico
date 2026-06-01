import streamlit as st
import requests

URL_BASE = "http://localhost:8080"

class AdminTemplate:

    @staticmethod
    @st.cache_data(ttl=5) # guarda a resposta na memória por 5 segundos
    def verificar_status_api(): # verifica a API a cada 5 segundos
        try:
            return requests.get(f"{URL_BASE}/health", timeout=0.5).status_code
        except Exception:
            return 500

    @staticmethod
    def redireciona_para_clientes():
        st.switch_page("template/admin/clientes.py")

    @staticmethod
    def renderizar_navegacao():

        # serve para contornar o problema da página default do streamlit que não mostra a URL correta da página
        pg_raiz = st.Page(AdminTemplate.redireciona_para_clientes, title="Início", default=True)

        pg_clientes = st.Page("template/admin/clientes.py", title="Clientes", icon="👥", url_path="admin-clientes")
        pg_categorias = st.Page("template/admin/categorias.py", title="Categorias", icon="🏷️", url_path="admin-categorias")
        pg_produtos = st.Page("template/admin/produtos.py", title="Produtos", icon="📦", url_path="admin-produtos")
        pg_vendas = st.Page("template/admin/vendas.py", title="Vendas", icon="💰", url_path="admin-vendas")

        # esconde a navegação padrão do Streamlit
        menu_admin = st.navigation([pg_raiz, pg_clientes, pg_categorias, pg_produtos, pg_vendas], position="hidden")

        with st.sidebar:
            st.subheader("Status do Sistema")

            if (AdminTemplate.verificar_status_api() == 200):
                st.success("🟢 Back-end Online")
            else:
                st.error("🔴 Back-end Offline")

            st.divider()

            # links de navegação
            st.page_link(pg_clientes)
            st.page_link(pg_categorias)
            st.page_link(pg_produtos)
            st.page_link(pg_vendas)

            # Botão de sair destacado no final da sidebar
            if (st.button("Sair", icon="🚪")):
                del st.session_state.id_usuario_logado # deleta variável para voltar à tela de login
                st.rerun()

        # roda a tela selecionada da navegação
        menu_admin.run()