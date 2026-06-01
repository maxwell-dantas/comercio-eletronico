import streamlit as st
import requests
import time

URL_BASE = "http://localhost:8080"

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
            st.switch_page("template/visitante/login.py")
        else:
            st.error(resposta.text)
