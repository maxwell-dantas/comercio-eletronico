import streamlit as st

from template.admin.crud_generico import CrudGenerico

class AdminClienteTemplate(CrudGenerico):
    def __init__(self):
        super().__init__("Gerenciamento de Clientes", "cliente", "clientes")

    # Override
    def formatar_selectbox(self, content):
        return f"{content['id']} - {content['nome']} - {content['email']}"

    # Override
    def inserir(self):
        nome = st.text_input("Nome")
        telefone = st.text_input("Telefone")
        email = st.text_input("E-mail")
        senha = st.text_input("Senha", type="password")

        tipo_conta = st.radio("Deseja criar conta como:", ["Admin", "Cliente", "Entregador"], horizontal=True)
        mapeamento_funcao = {"Admin": 1, "Cliente": 2, "Entregador": 3}
        id_funcao = mapeamento_funcao[tipo_conta]

        return {"nome": nome, "telefone": telefone, "email": email, "senha": senha, "idFuncao": id_funcao}

    # Override
    def atualizar(self, item_selecionado):
        nome_up = st.text_input("Nome", value=item_selecionado["nome"])
        telefone_up = st.text_input("Telefone", value=item_selecionado["telefone"])
        email_up = st.text_input("E-mail", value=item_selecionado["email"])
        senha_up = st.text_input("Nova Senha", type="password", placeholder="Digite apenas se quiser alterar")

        indice_atual = item_selecionado["idFuncao"] - 1
        tipo_conta_up = st.radio("Função:", ["Admin", "Cliente", "Entregador"], index=indice_atual, horizontal=True)

        mapeamento_funcao = {"Admin": 1, "Cliente": 2, "Entregador": 3}
        id_funcao_up = mapeamento_funcao[tipo_conta_up]

        # caso a senha for vazia (não foi alterada), pega a senha atual no banco de dados
        senha_up = senha_up if senha_up else item_selecionado["senha"]

        return {"nome": nome_up, "telefone": telefone_up, "email": email_up, "senha": senha_up, "idFuncao": id_funcao_up}

AdminClienteTemplate().menu()
