import streamlit as st
import requests
import time

URL_BASE = "http://localhost:8080"

class VisitanteTemplate:

    @staticmethod
    @st.cache_data(ttl=5) # Guarda a resposta na memória por 5 segundos
    def verificar_status_api(): # verifica a API a cada 5 segundos
        try:
            return requests.get(f"{URL_BASE}/health", timeout=0.5).status_code
        except Exception:
            return 500

    @staticmethod
    def ir_para_login():
        st.session_state.pagina_atual = "visitante-login"

    @staticmethod
    def ir_para_cadastro():
        st.session_state.pagina_atual = "visitante-cadastro"

    @staticmethod
    def ir_para_cliente():
        st.session_state.pagina_atual = "cliente-produtos"

    @staticmethod
    def ir_para_admin():
        st.session_state.pagina_atual = "admin-clientes"

    @staticmethod
    def menu():

        with st.sidebar:
            st.subheader("Status do Sistema")

            if (VisitanteTemplate.verificar_status_api() == 200):
                st.success("🟢 Back-end Online")
            else:
                st.error("🔴 Back-end Offline")

            st.divider()  # linha divisória

            entrar = st.button("Fazer Login", on_click=VisitanteTemplate.ir_para_login, use_container_width=True)
            criar_conta = st.button("Criar Conta", on_click=VisitanteTemplate.ir_para_cadastro, use_container_width=True)

        if (st.session_state.pagina_atual == "visitante-login"):
            st.title("Faça Login com sua conta Caju")

            with st.form("login"):
                email = st.text_input("E-mail")
                senha = st.text_input("Senha", type="password")
                btn_entrar = st.form_submit_button("Entrar")

                if (btn_entrar):
                    dados_login = {
                        "email": email,
                        "senha": senha
                    }

                    validacao = requests.post(f"{URL_BASE}/login", json=dados_login)

                    if (validacao.status_code == 200):
                        st.session_state.id_usuario_logado = validacao.json()["idUsuario"]

                        if (validacao.json()["idUsuario"] == 1):
                            VisitanteTemplate.ir_para_admin()
                        else:
                            VisitanteTemplate.ir_para_cliente()
                        st.rerun() # servirá para carregar a página com endereço menu cliente

        elif (st.session_state.pagina_atual == "visitante-cadastro"):
            st.title("Faça Cadastro no Mercadinho Caju")

            with st.form("cadastro"):
                nome = st.text_input("Nome")
                telefone = st.text_input("Telefone")
                email = st.text_input("E-mail")
                senha = st.text_input("Senha", type="password")
                btn_criar_conta = st.form_submit_button("Criar Conta")

                if (btn_criar_conta):
                    dados_cadastro = {
                        "nome": nome,
                        "telefone": telefone,
                        "email": email,
                        "senha": senha
                    }

                    resposta = requests.post(f"{URL_BASE}/clientes", json=dados_cadastro)
                    resposta.encoding = "utf-8"

                    if (resposta.status_code == 201):
                        st.success(resposta.text)
                        time.sleep(3)
                        VisitanteTemplate.ir_para_login()
                        st.rerun()
                    else:
                        st.error(resposta.text)
