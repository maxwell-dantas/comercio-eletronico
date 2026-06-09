import streamlit as st

from template.admin.crud_generico import CrudGenerico

st.set_page_config(page_title="Mercadinho Caju", page_icon="🛒", layout="wide")

class AdminCategoriaTemplate(CrudGenerico):
    def __init__(self):
        super().__init__("Gerenciamento de Categorias", "categoria", "categorias")

    # Override
    def formatar_selectbox(self, content):
        return f"{content['descricao']}"

    # Override
    def inserir(self):
        descricao = st.text_input("Descrição")
        return {"descricao": descricao}

    # Override
    def atualizar(self, item_selecionado):
        descricao_up = st.text_input("Descrição", value=item_selecionado["descricao"])
        return {"descricao": descricao_up}

AdminCategoriaTemplate().menu()