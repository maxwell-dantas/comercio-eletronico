import streamlit as st
import requests
from datetime import datetime

st.set_page_config(page_title="Mercadinho Caju - Histórico", page_icon="✅", layout="centered")

URL_BASE = "http://localhost:8080"

st.title("✅ Histórico de Entregas")
st.write("Consulte todos os pacotes que você já entregou com sucesso.")
st.markdown("---")

id_entregador_logado = st.session_state.get('id_usuario')

resposta_vendas = requests.get(f"{URL_BASE}/vendas")
resposta_vendas.encoding = "utf-8"

resposta_clientes = requests.get(f"{URL_BASE}/clientes")
dict_clientes = {}
if resposta_clientes.status_code == 200:
    dict_clientes = {c['id']: c['nome'] for c in resposta_clientes.json()}

if resposta_vendas.status_code == 200:
    todas_vendas = resposta_vendas.json()
    
    entregas_concluidas = [
        v for v in todas_vendas 
        if v.get("idEntregador") == id_entregador_logado and v.get("statusEntrega") == "ENTREGUE"
    ]
    
    if not entregas_concluidas:
        st.info("Você ainda não possui entregas finalizadas.")
    else:
        # ordem de visualização da última entrega para a primeira
        entregas_concluidas.sort(key=lambda x: x.get('dataEntrega') or x.get('data', ''), reverse=True)
        
        total = len(entregas_concluidas)
        st.success(f"Você já concluiu um total de **{total}** entregas!")
        
        for indice, entrega in enumerate(entregas_concluidas, start=1):
            id_cliente = entrega.get('idCliente')
            nome_cliente = dict_clientes.get(id_cliente, f"Cliente ID: {id_cliente}")
            
            try:
                # Usa a data de entrega se existir, senão usa a data do pedido
                data_referencia = entrega.get('dataEntrega', entrega['data'])
                data_obj = datetime.strptime(data_referencia[:16], "%Y-%m-%dT%H:%M")
                data_formatada = data_obj.strftime("%d/%m/%Y às %H:%M")
            except:
                data_formatada = "Data indisponível"
            
            # exibe a numeração exata em ordem decrescente (Ex: 6, 5, 4...)
            numero_exato_da_entrega = total - indice + 1
            
            with st.container(border=True):
                st.markdown(f"### Entrega nº {numero_exato_da_entrega}")
                st.write(f"**Cliente:** {nome_cliente} | **Pedido:** #{entrega['id']}")
                st.caption(f"Finalizado em: {data_formatada} | **Status:** 🟢 Entregue")
else:
    st.error("Erro ao conectar com o servidor.")