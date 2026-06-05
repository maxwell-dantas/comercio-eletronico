import streamlit as st
import requests
from template.admin.crud_generico import CrudGenerico

URL_BASE = "http://localhost:8080"

class AdminProdutoTemplate(CrudGenerico):
    def __init__(self):
        super().__init__("Gerenciamento de Produtos", "produto", "produtos")

    def _obter_categorias(self):
        resposta = requests.get(f"{URL_BASE}/categorias")
        if resposta.status_code == 200:
            return resposta.json()
        return []

    # Override
    def formatar_selectbox(self, content):
        return f"{content['descricao']}"

    # Override
    def inserir(self):
        descricao = st.text_input("Descrição do Produto")
        preco = st.number_input("Preço (R$)", min_value=0.0, format="%.2f")
        estoque = st.number_input("Estoque", min_value=0, step=1)

        st.markdown("---")

        categorias = self._obter_categorias()

        if not categorias:
            st.warning("⚠️ Não existem categorias cadastradas. Por favor, adicione categorias primeiro.")
            return None

        categoria_selecionada = st.selectbox(
            "Selecione a Categoria:",
            options=categorias,
            format_func=self.formatar_selectbox
        )

        return {
            "descricao": descricao,
            "preco": preco,
            "estoque": estoque,
            "idCategoria": categoria_selecionada["id"]
        }

    # Override
    def atualizar(self, item_selecionado):
        descricao_up = st.text_input("Descrição do Produto", value=item_selecionado["descricao"])
        preco_up = st.number_input("Preço (R$)", min_value=0.0, value=float(item_selecionado["preco"]), format="%.2f")
        estoque_up = st.number_input("Estoque", min_value=0, value=int(item_selecionado["estoque"]), step=1)

        categorias = self._obter_categorias()
        st.markdown("---")


        if not categorias:
            st.warning("Nenhuma categoria encontrada.")
            return None
        else:
            indice_categoria_atual = 0
            for index, cat in enumerate(categorias):
                if cat["id"] == item_selecionado["idCategoria"]:
                    indice_categoria_atual = index
                    break

            categoria_selecionada_up = st.selectbox(
                "Selecione a nova Categoria:",
                options=categorias,
                index=indice_categoria_atual,
                format_func=self.formatar_selectbox,
                key=f"select_cat_upd_{item_selecionado['id']}"
            )
            id_categoria_up = categoria_selecionada_up["id"]

        return {
            "descricao": descricao_up,
            "preco": preco_up,
            "estoque": estoque_up,
            "idCategoria": id_categoria_up
        }

AdminProdutoTemplate().menu()