import streamlit as st
import requests
import time

URL_BASE = "http://localhost:8080"

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
        validacao.encoding = "utf-8"

        if (validacao.status_code == 200):
            st.session_state.id_usuario = validacao.json()["id"] # captura o ID do usuário
            st.session_state.id_funcao = validacao.json()["idFuncao"] # captura o ID da funcão do usuário
            st.session_state.nome_usuario = validacao.json()["nome"] # captura o nome do usuário
            st.rerun() # servirá para carregar a página referente ao usuário
        else:
            st.error(validacao.text)
            time.sleep(2)
            st.rerun()
