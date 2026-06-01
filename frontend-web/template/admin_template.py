import streamlit as st
import requests

URL_BASE = "http://localhost:8080"

class AdminTemplate:

    @staticmethod
    @st.cache_data(ttl=5) # Guarda a resposta na memória por 5 segundos
    def verificar_status_api(): # verifica a API a cada 5 segundos
        try:
            return requests.get(f"{URL_BASE}/health", timeout=0.5).status_code
        except Exception:
            return 500

    @staticmethod
    def ir_para_clientes():
        st.session_state.pagina_atual = "admin-clientes"

    @staticmethod
    def ir_para_categorias():
        st.session_state.pagina_atual = "admin-categorias"

    @staticmethod
    def ir_para_produtos():
        st.session_state.pagina_atual = "admin-produtos"

    @staticmethod
    def ir_para_vendas():
        st.session_state.pagina_atual = "admin-vendas"

    @staticmethod
    def logout():
        del st.session_state.id_usuario_logado
        st.session_state.pagina_atual = "visitante-login"

    @staticmethod
    def menu():
        with st.sidebar:
            st.subheader("Status do Sistema")

            if (AdminTemplate.verificar_status_api() == 200):
                st.success("🟢 Back-end Online")
            else:
                st.error("🔴 Back-end Offline")

            st.divider()  # linha divisória

            st.button("Clientes", on_click=AdminTemplate.ir_para_clientes, use_container_width=True)
            st.button("Categorias", on_click=AdminTemplate.ir_para_categorias, use_container_width=True)
            st.button("Produtos", on_click=AdminTemplate.ir_para_produtos, use_container_width=True)
            st.button("Vendas", on_click=AdminTemplate.ir_para_vendas, use_container_width=True)
            st.button("Sair", on_click=AdminTemplate.logout, use_container_width=True)

        if (st.session_state.pagina_atual == "admin-clientes"):
            pass
        elif (st.session_state.pagina_atual == "admin-categorias"):
            pass
        elif (st.session_state.pagina_atual == "admin-produtos"):
            pass
        elif (st.session_state.pagina_atual == "admin-vendas"):
            st.title("Histórico de Vendas")

            vendas = requests.get(f"{URL_BASE}/vendas")
            venda_itens = requests.get(f"{URL_BASE}/venda_itens")

            if (vendas.status_code == 200 and venda_itens.status_code == 200):
                pass
            else:
                st.write("Ainda não foi iniciada nenhuma venda!")