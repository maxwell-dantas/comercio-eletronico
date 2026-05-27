import streamlit as st
import requests

# 1. Configuração da página
st.set_page_config(page_title="Comércio Eletrônico", layout="wide")

# 2. Configuração da API (A URL Base)
# Substitua pela sua URL real, mas DEIXE SEM A BARRA NO FINAL!
URL_BASE = "https://fuzzy-space-enigma-q7qxrgrprx4x3676-8080.app.github.dev"

# 3. Criando o Menu Lateral interativo
with st.sidebar:
    # --- NOVO: Verificador de Health Check ---
    st.subheader("Status do Sistema")
    try:
        # Bate na rota /health e espera no máximo 2 segundos
        verificacao = requests.get(f"{URL_BASE}/health", timeout=2)
        
        if verificacao.status_code == 200:
            st.success("🟢 Back-end Online")
        else:
            st.warning("🟡 API instável")
    except Exception:
        # Se der erro de conexão (ex: você esqueceu de rodar o Java)
        st.error("🔴 Back-end Offline")
        
    st.divider() # Cria uma linha divisória visual
    
    # --- O Menu de Navegação Original ---
    st.header("Navegação")
    menu_selecionado = st.radio(
        "Selecione a página:",
        ["📦 Produtos", "👥 Clientes", "🏷️ Categorias"]
    )

# 4. Lógica de Navegação e Consumo da API
st.title("Gestão do Sistema")

if menu_selecionado == "📦 Produtos":
    st.subheader("Lista de Produtos em Estoque")
    
    # Monta a URL completa juntando a base com a rota específica
    resposta = requests.get(f"{URL_BASE}/produtos")
    
    if resposta.status_code == 200:
        st.dataframe(resposta.json(), use_container_width=True)
    else:
        st.error("Erro ao buscar produtos.")

elif menu_selecionado == "👥 Clientes":
    st.subheader("Cadastro de Clientes")
    
    resposta = requests.get(f"{URL_BASE}/clientes")
    
    if resposta.status_code == 200:
        st.dataframe(resposta.json(), use_container_width=True)
    else:
        st.error("Erro ao buscar clientes.")

elif menu_selecionado == "🏷️ Categorias":
    st.subheader("Categorias do Sistema")
    
    resposta = requests.get(f"{URL_BASE}/categorias")
    
    if resposta.status_code == 200:
        st.dataframe(resposta.json(), use_container_width=True)
    else:
        st.error("Erro ao buscar categorias.")