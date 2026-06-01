import streamlit as st

from template.admin_template import AdminTemplate
from template.visitante_template import VisitanteTemplate

st.set_page_config(page_title="Comércio Eletrônico", layout="centered")

# verificação de sessões
if "pagina_atual" not in st.session_state:
    VisitanteTemplate.ir_para_login()

if (st.session_state.pagina_atual in ["visitante-login", "visitante-cadastro"]):
    VisitanteTemplate.menu()

elif (st.session_state.pagina_atual in ["admin-clientes", "admin-categorias", "admin-produtos", "admin-vendas"]):
    AdminTemplate.menu()

elif (st.session_state.pagina_atual in ["cliente-produtos", "cliente-carrinho", "cliente-historico-de-compras"]):
    pass