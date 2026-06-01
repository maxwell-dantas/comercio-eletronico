import streamlit as st

from template.visitante.template import VisitanteTemplate

st.set_page_config(page_title="Mercadinho Caju", page_icon="🛒", layout="centered")

if "id_usuario_logado" not in st.session_state: # caso não possua nenhum usuário logado, entra na tela de visitante
    VisitanteTemplate.renderizar_navegacao()

elif st.session_state.id_usuario_logado == 1: # verifica se o usuário é admin
    # AdminTemplate.renderizar_navegacao()
    st.title("⚙️ Bem-vindo, Admin!") # Placeholder para testes
    if st.button("Sair"):
        del st.session_state.id_usuario_logado
        st.rerun()

# qualquer ID maior que 1 é cliente
else:
    # ClienteTemplate.renderizar_navegacao()
    st.title("🛒 Bem-vindo, Cliente!") # Placeholder para testes
    st.write(f"Seu ID é: {st.session_state.id_usuario_logado}")
    if st.button("Sair"):
        del st.session_state.id_usuario_logado
        st.rerun()