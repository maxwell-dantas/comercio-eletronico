import streamlit as st

from template.admin.template import AdminTemplate
from template.visitante.template import VisitanteTemplate
from template.entregador.template import EntregadorTemplate
from template.cliente.template import ClienteTemplate

st.set_page_config(page_title="Mercadinho Caju", page_icon="🛒", layout="centered")

if "id_usuario" not in st.session_state: # caso não possua nenhum usuário logado, entra na tela de visitante
    VisitanteTemplate.renderizar_navegacao()

elif st.session_state.id_funcao == 1: # verifica se o usuário é admin
    AdminTemplate.renderizar_navegacao()

elif st.session_state.id_funcao == 2:
    ClienteTemplate.renderizar_navegacao()
        
elif st.session_state.id_funcao == 3:
    EntregadorTemplate.renderizar_navegacao()
