import streamlit as st
import requests
import time

URL_BASE = "http://localhost:8080"



def buscar_catalogo():
    try:
        # busca informações na api
        resposta = requests.get(f"{URL_BASE}/produtos", timeout=5)

        if resposta.status_code == 200: #Status favorável 
            return resposta.json()
        
        elif resposta.status_code == 404:
            # resposta.text vai pegar o conteúdo de 'e.getMessage()' do Java
            st.warning(f"Aviso do servidor: {resposta.text}")
            return []
        
        # Para outros erros (500, etc), força uma exceção
        else:
            resposta.raise_for_status()
    except requests.exceptions.RequestException as e:

        # Cai aqui se o servidor Java estiver desligaDO
        st.error(f"Erro ao tentar conectar com o servidor Java. Verifique se ele está rodando na porta 8080. Detalhes: {e}")
        return []
    
def enviar_para_carrinho(produto_id, nome_produto, quantidade_selecionada):
    """Envia o pedido finalizado para o Java"""
    # Exemplo de como ficará o POST depois:
    # payload = {"produto_id": produto_id, "quantidade": quantidade_selecionada}
    # resposta = requests.post(f"{API_BASE_URL}/carrinho", json=payload)
    st.success(f"{quantidade_selecionada}x {nome_produto} adicionado(s) ao carrinho!")
        




st.set_page_config(page_title="Comércio Eletrônico", layout="centered")

st.header("Veja nossos produtos disponíveis")
st.divider()

produtos = buscar_catalogo()

#Se não tiver nada, para
if not produtos:
    st.stop()


for produto in produtos:
    # Cria um container em volta da linha toda para dar o efeito visual de bloco
    with st.container(border=True):
        col_info, col_qtd, col_btn = st.columns([5, 2, 1], vertical_alignment="center")
        
        esgotado = produto["estoque"] <= 0
        
        with col_info:
            st.markdown(f"**{produto['descricao']}**")
            st.caption(f"R$ {produto['preco']:.2f} | Estoque: {produto['estoque']}")
            
        with col_qtd:
            # A caixa de quantidade fica no meio, bem compacta
            qtd = st.number_input(
                "Quantidade", 
                min_value=0 if esgotado else 1, 
                max_value=produto["estoque"], 
                step=1, 
                key=f"qtd_{produto['id']}",
                disabled=esgotado,
                label_visibility="collapsed" # Esconde o nome para economizar espaço
            )

        
        
        with col_btn:
            carrinho = st.button("🛒", key=f"add_{produto['id']}", disabled=esgotado, use_container_width=True)

        if carrinho:
            enviar_para_carrinho(produto["id"], produto["descricao"], qtd)
            time.sleep(2)
            st.rerun()