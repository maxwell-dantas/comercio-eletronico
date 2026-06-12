import streamlit as st
import requests
import time
import base64 # <-- Nova importação obrigatória para lidar com as imagens

URL_BASE = "http://localhost:8080"

def buscar_catalogo():
    try:
        resposta = requests.get(f"{URL_BASE}/produtos", timeout=5)

        if resposta.status_code == 200: 
            return resposta.json()
        
        elif resposta.status_code == 404:
            st.warning(f"Aviso do servidor: {resposta.text}")
            return []
        
        else:
            resposta.raise_for_status()
    except requests.exceptions.RequestException as e:
        st.error(f"Erro ao tentar conectar com o servidor Java. Verifique se ele está rodando na porta 8080. Detalhes: {e}")
        return []
    
def enviar_para_carrinho(produto_id, nome_produto, quantidade_selecionada):
    st.success(f"{quantidade_selecionada}x {nome_produto} adicionado(s) ao carrinho!")
        

st.set_page_config(page_title="Comércio Eletrônico", layout="centered")

st.header("Veja nossos produtos disponíveis")
st.divider()

produtos = buscar_catalogo()

if not produtos: #Para se não houver produtos ou em caso de erro da API
    st.stop()

for produto in produtos:
    with st.container(border=True):
    
        col_img, col_info, col_qtd, col_btn = st.columns([2, 4, 2, 1], vertical_alignment="center")
        
        esgotado = produto["estoque"] <= 0 # Verifica se o produto está esgotado para desabilitar a compra
        
        
        with col_img:
            # Verifica se a chave existe e se não é nula/vazia
            img_base64 = produto.get("imagemBase64")
            
            if img_base64:
                try:
                    # Traduz o texto Base64 para os bytes da imagem e exibe
                    imagem_bytes = base64.b64decode(img_base64)
                    st.image(imagem_bytes, use_container_width=True)
                except Exception:
                    # Se vier um texto inválido do Java que não seja imagem
                    st.image("https://placehold.co/150x150?text=Erro+na+Imagem", use_container_width=True)
            else:
                # Placeholder elegante para quando a imagem for 'null'
                st.image("https://placehold.co/150x150?text=Sem+Imagem", use_container_width=True)
                
        # INFORMAÇÕES DO PRODUTO 
        with col_info:
            st.markdown(f"**{produto['descricao']}**")
            st.caption(f"R$ {produto['preco']:.2f} | Estoque: {produto['estoque']}")
            
        with col_qtd:
            qtd = st.number_input(
                "Quantidade", 
                min_value=0 if esgotado else 1, 
                max_value=produto["estoque"], 
                step=1, 
                key=f"qtd_{produto['id']}",
                disabled=esgotado,
                label_visibility="collapsed" 
            )
        
        with col_btn:
            carrinho = st.button("🛒", key=f"add_{produto['id']}", disabled=esgotado, use_container_width=True)

        if carrinho:
            enviar_para_carrinho(produto["id"], produto["descricao"], qtd)
            time.sleep(2)
            st.rerun()