import streamlit as st

from template.admin.crud_generico import CrudGenerico

class AdminCategoriaTemplate(CrudGenerico):
    def __init__(self):
        super().__init__("Gerenciamento de Categorias", "categoria", "categorias")

    def formatar_selectbox(self, content):
        return f"{content['id']} - {content['descricao']}"

    def inserir(self):
        descricao = st.text_input("Descrição")
        return {"descricao": descricao}

    def atualizar(self, item_selecionado):
        descricao_up = st.text_input("Descrição", value=item_selecionado["descricao"])
        return {"descricao": descricao_up}

AdminCategoriaTemplate().menu()