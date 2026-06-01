import streamlit as st
import requests

# Configuração da página
st.set_page_config(page_title="Comércio Eletrônico", layout="centered")

URL_BASE = "http://localhost:8080"


# FUNÇÕES PLACEHOLDERS

def pagina_carrinho():
    st.header("🛒 Meu Carrinho")
    st.info("Página em construção! Aqui você verá os itens que selecionou.")

def pagina_historico():
    st.header("🧾 Histórico de Compras")
    st.info("Página em construção! Suas compras anteriores serão listadas aqui.")

def redirecionar_para_produtos():
    # Esta função só serve para jogar o usuário para a URL /produtos
    st.switch_page(pg_produtos)


# DEFININDO ROTAS PERSONALIZADAS

# página raiz (default) invisível
pg_raiz = st.Page(
    redirecionar_para_produtos, 
    title="Início", 
    default=True 
)

pg_produtos = st.Page(
    "catalogo_produtos.py", 
    title="Produtos Disponíveis", 
    icon="📦",
    url_path="produtos" 
)

pg_carrinho = st.Page(
    pagina_carrinho, 
    title="Meu Carrinho", 
    icon="🛒",
    url_path="carrinho"
)

pg_historico = st.Page(
    pagina_historico, 
    title="Histórico de Compras", 
    icon="🧾",
    url_path="historico"
)


# NAVEGAÇÃO E SIDEBAR


menu_navegacao = st.navigation([pg_raiz, pg_produtos,  pg_carrinho, pg_historico], position="hidden")

with st.sidebar:
    st.header("Seja bem-vindo!")
    
    st.page_link(pg_produtos)
    st.page_link(pg_carrinho)
    st.page_link(pg_historico)

menu_navegacao.run()