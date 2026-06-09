import streamlit as st
import requests
import time
import base64

from template.admin.crud_generico import CrudGenerico

st.set_page_config(page_title="Mercadinho Caju", page_icon="🛒", layout="wide")

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
        imagem_base64 = st.file_uploader("Adicione uma imagem (Opcional)", type=["png", "jpg", "jpeg"])

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

        imagem_final_inserir = None
        if imagem_base64:
            bytes_data = imagem_base64.getvalue()
            imagem_final_inserir = base64.b64encode(bytes_data).decode("utf-8")

        return {
            "descricao": descricao,
            "preco": preco,
            "estoque": estoque,
            "idCategoria": categoria_selecionada["id"],
            "imagemBase64": imagem_final_inserir
        }

    # Override
    def atualizar(self, item_selecionado):
        descricao_up = st.text_input("Descrição do Produto", value=item_selecionado["descricao"])
        preco_up = st.number_input("Preço (R$)", min_value=0.0, value=float(item_selecionado["preco"]), format="%.2f")
        estoque_up = st.number_input("Estoque", min_value=0, value=int(item_selecionado["estoque"]), step=1)
        
        imagem_atual = item_selecionado.get("imagemBase64")
        
        if imagem_atual:
            st.write("Imagem atual:")
            st.image(base64.b64decode(imagem_atual), width=150)
            
        imagem_up = st.file_uploader("Substituir imagem (deixe em branco para manter a atual)", type=["png", "jpg", "jpeg"], key=f"upd_{item_selecionado['id']}")

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

        if imagem_up:
            bytes_data = imagem_up.getvalue()
            imagem_final = base64.b64encode(bytes_data).decode("utf-8")
        else:
            imagem_final = imagem_atual

        return {
            "descricao": descricao_up,
            "preco": preco_up,
            "estoque": estoque_up,
            "idCategoria": id_categoria_up,
            "imagemBase64": imagem_final
        }

    # Override
    def get_abas_extras(self):
        return ["Promoções", "Aumento"] 

    # Override
    def renderizar_abas_extras(self, abas_extras):

        aba_promocao = abas_extras[0]
        aba_aumento = abas_extras[1]
        
        # PROMOÇÃO
        with aba_promocao:
            st.subheader("🎉 Configurar Promoções por Categoria")
            st.write("Defina um período promocional e o desconto para os produtos de uma categoria.")
            
            with st.container(border=True):
                categorias = self._obter_categorias()
                
                if not categorias:
                    st.warning("Cadastre categorias antes de criar promoções.")
                else:
                    cat_selecionada = st.selectbox(
                        "Categoria Alvo", 
                        options=categorias, 
                        format_func=self.formatar_selectbox,
                        key="cat_promo"
                    )
                    
                    col1, col2 = st.columns(2)
                    with col1:
                        data_inicio = st.date_input("Data de Início")
                    with col2:
                        data_fim = st.date_input("Data de Encerramento")
                        
                    desconto = st.number_input("Percentual de Desconto (%)", min_value=1.0, max_value=99.0, step=1.0)
                    
                    if st.button("Aplicar Promoção"):
                        # Monta o JSON exato da Entidade Promocao
                        dados_promocao = {
                            "idCategoria": cat_selecionada["id"],
                            "dataInicio": str(data_inicio), # Converte para string ISO (YYYY-MM-DD)
                            "dataFim": str(data_fim),
                            "percentualDesconto": float(desconto)
                        }
                        
                        resposta = requests.post(f"{URL_BASE}/promocoes", json=dados_promocao)
                        
                        if resposta.status_code == 201:
                            st.success(f"A promoção para a categoria {cat_selecionada['descricao']} foi agendada e está ativa no sistema!")
                            time.sleep(2)
                            st.rerun()
                        else:
                            st.error(resposta.text)

        # AUMENTO
        with aba_aumento:
            st.subheader("📈 Configurar Aumento por Categoria")
            st.write("Defina um aumento permanente para os produtos de uma categoria.")
            
            with st.container(border=True):
                categorias = self._obter_categorias()
                
                if not categorias:
                    st.warning("Cadastre categorias antes de realizar aumento por categorias.")
                else:
                    cat_selecionada_aumento = st.selectbox(
                        "Categoria Alvo", 
                        options=categorias, 
                        format_func=self.formatar_selectbox,
                        key="cat_aum"
                    )
                        
                    aumento = st.number_input("Percentual de Aumento (%)", min_value=1.0, step=1.0)
                    
                    if st.button("Aplicar Aumento"):
                        url = f"{URL_BASE}/produtos/categoria/{cat_selecionada_aumento['id']}/aumento?porcentagem={aumento}"
                        resposta = requests.put(url)
                        
                        if resposta.status_code == 200:
                            st.success(f"O aumento de {aumento}% foi aplicado e todos os preços da categoria {cat_selecionada_aumento['descricao']} foram reajustados!")
                            time.sleep(2)
                            st.rerun()
                        else:
                            st.error(resposta.text)
    
AdminProdutoTemplate().menu()