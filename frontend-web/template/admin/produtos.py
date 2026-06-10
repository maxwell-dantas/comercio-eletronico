import streamlit as st
import requests
import time
import base64
from datetime import datetime

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
    def formatar_dataframe(self, df):
        categorias = self._obter_categorias()
        dict_cat = {c['id']: c['descricao'] for c in categorias}

        promocoes = []
        resp_promo = requests.get(f"{URL_BASE}/promocoes")
        if resp_promo.status_code == 200:
            promocoes = resp_promo.json()

        hoje = datetime.now().date()

        nomes_categorias = []
        precos_finais = []
        status_promo = []

        for index, row in df.iterrows():
            id_cat = row['idCategoria']
            preco_base = float(row['preco'])
            
            nomes_categorias.append(dict_cat.get(id_cat, "Desconhecida"))

            promo_ativa = None
            for p in promocoes:
                if p['idCategoria'] == id_cat:
                    dt_inicio = datetime.strptime(p['dataInicio'], "%Y-%m-%d").date()
                    dt_fim = datetime.strptime(p['dataFim'], "%Y-%m-%d").date()
                    if dt_inicio <= hoje <= dt_fim:
                        promo_ativa = p
                        break
            
            if promo_ativa:
                desconto = promo_ativa['percentualDesconto']
                preco_com_desconto = preco_base * (1 - (desconto / 100.0))
                precos_finais.append(f"R$ {preco_com_desconto:.2f}")
                status_promo.append(f"🔥 Ativa (-{desconto}%)")
            else:
                precos_finais.append(f"R$ {preco_base:.2f}")
                status_promo.append("Desativado")

        df['Categoria'] = nomes_categorias
        df['Preço Promocional'] = precos_finais
        df['Status'] = status_promo

        df['preco'] = df['preco'].apply(lambda x: f"R$ {x:.2f}")
        df = df.rename(columns={
            'descricao': 'Descrição do Produto',
            'preco': 'Preço Base',
            'estoque': 'Estoque'
        })
        
        colunas_ordem = ['id', 'Descrição do Produto', 'Categoria', 'idCategoria', 'Preço Base', 'Preço Promocional', 'Status', 'Estoque']
        if 'imagemBase64' in df.columns:
            colunas_ordem.append('imagemBase64')
            
        return df[colunas_ordem]

    # Override
    def formatar_selectbox(self, content):
        descricao = content.get('descricao', 'Sem Descrição')
        return f"{descricao}"

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
            format_func=lambda cat: cat['descricao']
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
        descricao_up = st.text_input("Descrição do Produto", value=item_selecionado.get("descricao", ""))

        preco_atual = float(item_selecionado.get("preco", 0.0))
        preco_up = st.number_input("Preço (R$)", min_value=0.0, value=preco_atual, format="%.2f")
        
        estoque_up = st.number_input("Estoque", min_value=0, value=int(item_selecionado.get("estoque", 0)), step=1)
        
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
                if cat["id"] == item_selecionado.get("idCategoria", 0):
                    indice_categoria_atual = index
                    break

            categoria_selecionada_up = st.selectbox(
                "Selecione a nova Categoria:",
                options=categorias,
                index=indice_categoria_atual,
                format_func=lambda cat: cat['descricao'],
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
            
            categorias = self._obter_categorias()
            
            with st.form("form_criar_promocao"):
                if not categorias:
                    st.warning("Cadastre categorias antes de criar promoções.")
                    cat_selecionada = None
                else:
                    cat_selecionada = st.selectbox(
                        "Categoria Alvo", 
                        options=categorias, 
                        format_func=lambda cat: cat['descricao'],
                    )
                    
                    col1, col2 = st.columns(2)
                    with col1:
                        data_inicio = st.date_input("Data de Início", format="DD/MM/YYYY")
                    with col2:
                        data_fim = st.date_input("Data de Encerramento", format="DD/MM/YYYY")
                        
                    desconto = st.number_input("Percentual de Desconto (%)", min_value=1.0, max_value=99.0, step=1.0)
                    
                if st.form_submit_button("Aplicar Promoção"):
                    if cat_selecionada:
                        dados_promocao = {
                            "idCategoria": cat_selecionada["id"],
                            "dataInicio": str(data_inicio), 
                            "dataFim": str(data_fim),
                            "percentualDesconto": float(desconto)
                        }
                        
                        resposta = requests.post(f"{URL_BASE}/promocoes", json=dados_promocao)
                        resposta.encoding = "utf-8"
                        
                        if resposta.status_code == 201:
                            st.success(f"A promoção para a categoria {cat_selecionada['descricao']} foi agendada!")
                            time.sleep(1.5)
                            st.rerun()
                        else:
                            st.error(resposta.text)

            st.markdown("---")
            st.subheader("📋 Gerenciar Promoções Ativas")
            
            resp_promo = requests.get(f"{URL_BASE}/promocoes")
            if resp_promo.status_code == 200:
                lista_promocoes = resp_promo.json()
                
                if not lista_promocoes:
                    st.info("Não há promoções ativas no momento.")
                else:
                    dict_cat = {c['id']: c['descricao'] for c in categorias} if categorias else {}
                    
                    for promo in lista_promocoes:
                        nome_categoria = dict_cat.get(promo['idCategoria'], "Categoria Removida")
                        
                        data_inicio_br = datetime.strptime(promo['dataInicio'], "%Y-%m-%d").strftime("%d/%m/%Y")
                        data_fim_br = datetime.strptime(promo['dataFim'], "%Y-%m-%d").strftime("%d/%m/%Y")
                        
                        with st.container(border=True):
                            col_info, col_btn = st.columns([4, 1])
                            
                            with col_info:
                                st.write(f"**Categoria:** {nome_categoria} | **Desconto:** 🔥 {promo['percentualDesconto']}%")
                                st.write(f"**Período:** {data_inicio_br} até {data_fim_br}")
                                
                            with col_btn:
                                st.markdown("<div style='margin-top: 10px;'></div>", unsafe_allow_html=True)
                                if st.button("❌ Desfazer", key=f"del_promo_{promo['id']}", use_container_width=True):
                                    res_del = requests.delete(f"{URL_BASE}/promocoes/{promo['id']}")
                                    res_del.encoding = "utf-8"
                                    
                                    if res_del.status_code == 200:
                                        st.success("Promoção cancelada!")
                                        time.sleep(1.5)
                                        st.rerun()
                                    else:
                                        st.error(res_del.text)
            else:
                st.error("Não foi possível carregar as promoções.")

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
                        format_func=lambda cat: cat['descricao'],
                        key="cat_aum"
                    )
                        
                    aumento = st.number_input("Percentual de Aumento (%)", min_value=1.0, step=1.0)
                    
                    if st.button("Aplicar Aumento"):
                        url = f"{URL_BASE}/produtos/categoria/{cat_selecionada_aumento['id']}/aumento?porcentagem={aumento}"
                        resposta = requests.put(url)
                        resposta.encoding = "utf-8"
                        
                        if resposta.status_code == 200:
                            st.success(f"O aumento de {aumento}% foi aplicado e todos os preços reajustados!")
                            time.sleep(1.5)
                            st.rerun()
                        else:
                            st.error(resposta.text)
    
AdminProdutoTemplate().menu()