import streamlit as st
import requests
import time
from datetime import datetime

st.set_page_config(page_title="Mercadinho Caju - Entregas Pendentes", page_icon="📦", layout="centered")

URL_BASE = "http://localhost:8080"

st.title(f"📦 Entregas Pendentes")
st.write(f"Olá, **{st.session_state.get('nome_usuario', 'Parceiro')}**! Estas são as suas rotas ativas.")
st.markdown("---")

id_entregador_logado = st.session_state.get('id_usuario')

resposta_vendas = requests.get(f"{URL_BASE}/vendas")
resposta_vendas.encoding = "utf-8"

resposta_clientes = requests.get(f"{URL_BASE}/clientes")
resposta_clientes.encoding = "utf-8"
dict_clientes = {}
if resposta_clientes.status_code == 200:
    dict_clientes = {c['id']: c['nome'] for c in resposta_clientes.json()}

if resposta_vendas.status_code == 200:
    todas_vendas = resposta_vendas.json()

    minhas_entregas = [
        v for v in todas_vendas
        if v.get("idEntregador") == id_entregador_logado and v.get("statusEntrega") == "EM_ROTA"
    ]

    if not minhas_entregas:
        st.success("🎉 Você não possui nenhuma entrega pendente no momento. Bom descanso!")
    else:
        # ORDENAÇÃO CRONOLÓGICA: Pedidos comprados primeiro aparecem no topo da fila
        minhas_entregas.sort(key=lambda x: x.get('data', ''))

        for indice, entrega in enumerate(minhas_entregas, start=1):
            id_cliente = entrega.get('idCliente')
            nome_cliente = dict_clientes.get(id_cliente, f"Cliente ID: {id_cliente}")

            try:
                data_obj = datetime.strptime(entrega['data'][:16], "%Y-%m-%dT%H:%M")
                data_formatada = data_obj.strftime("%d/%m/%Y %H:%M")
            except:
                data_formatada = "Data indisponível"

            with st.container(border=True):
                st.markdown(f"### Entrega Pendente nº {indice}")
                st.write(f"**Cliente:** {nome_cliente} | **Pedido:** #{entrega['id']}")
                st.caption(f"Data da compra: {data_formatada}")

                if st.button("✅ Confirmar Entrega Realizada", key=f"btn_entregar_{entrega['id']}", type="primary", use_container_width=True):
                    url_confirmar = f"{URL_BASE}/vendas/{entrega['id']}/finalizar-entrega"
                    res_confirmar = requests.put(url_confirmar)
                    res_confirmar.encoding = "utf-8"

                    if res_confirmar.status_code == 200:
                        st.success("Entrega finalizada com sucesso!")
                        time.sleep(1)
                        st.rerun()
                    else:
                        st.error(res_confirmar.text)
else:
    st.error("Erro ao conectar com o servidor.")