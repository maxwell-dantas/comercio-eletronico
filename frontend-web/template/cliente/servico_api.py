import streamlit as st
from datetime import datetime
import requests

class ServicoProdutosAPI:
    URL_BASE = "http://localhost:8080"

    @staticmethod
    def buscar_catalogo():
        try:
            resposta = requests.get(f"{ServicoProdutosAPI.URL_BASE}/produtos", timeout=5)

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
        
    @staticmethod
    def buscar_promocao():
        try:
            resposta = requests.get(f"{ServicoProdutosAPI.URL_BASE}/promocoes", timeout=5)

            if resposta.status_code == 200:
                return resposta.json()
            elif resposta.status_code == 404:
                st.warning(f"Aviso do servidor {resposta.text}")
            else:
                resposta.raise_for_status()

        except requests.exceptions.RequestException as e:
            st.error(f"Erro ao tentar conectar com o servidor Java. Verifique se ele está rodando na porta 8080. Detalhes: {e}")
            return []

######## PARTE DO CARRINHO ##########
class ServicoCarrinhoAPI:
    URL_BASE = "http://localhost:8080"

    @staticmethod
    def inicializar_carrinho(id_cliente):
        try:
            resposta = requests.get(f"{ServicoCarrinhoAPI.URL_BASE}/carrinho/{id_cliente}", timeout=5)
            if resposta.status_code == 200:
                venda = resposta.json()
                return venda["id"] 
            else:
                st.warning(f"Aviso do servidor ao abrir carrinho: {resposta.text}")
                return None
        except requests.exceptions.RequestException as e:
            st.error(f"Erro ao conectar com a API do Carrinho. Detalhes: {e}")
            return None

    @staticmethod
    def adicionar_item(id_venda, id_produto, quantidade):
        payload = {
            "idProduto": id_produto,
            "quantidade": quantidade
        }
        try:
            resposta = requests.post(f"{ServicoCarrinhoAPI.URL_BASE}/carrinho/{id_venda}/itens", json=payload, timeout=5)
            return resposta.status_code, resposta.text
        except requests.exceptions.RequestException as e:
            return 500, f"Erro de conexão: {e}"

    @staticmethod
    def buscar_itens(id_venda):
        try:
            resposta = requests.get(f"{ServicoCarrinhoAPI.URL_BASE}/carrinho/{id_venda}/itens", timeout=5)
            if resposta.status_code == 200:
                return resposta.json()
            return []
        except requests.exceptions.RequestException as e:
            st.error(f"Erro ao buscar os itens do carrinho. Detalhes: {e}")
            return []

    @staticmethod
    def limpar_carrinho(id_venda):
        try:
            resposta = requests.delete(f"{ServicoCarrinhoAPI.URL_BASE}/carrinho/{id_venda}/limpar", timeout=5)
            return resposta.status_code, resposta.text
        except requests.exceptions.RequestException as e:
            return 500, f"Erro de conexão: {e}"

    @staticmethod
    def finalizar_compra(id_venda):

        data_atual = datetime.now().strftime("%Y-%m-%dT%H:%M:%S")
        payload = {
            "carrinho": False,
            "data": data_atual
        }
        try:
            resposta = requests.post(f"{ServicoCarrinhoAPI.URL_BASE}/carrinho/{id_venda}/finalizar", json=payload, timeout=5)
            return resposta.status_code, resposta.text
        except requests.exceptions.RequestException as e:
            return 500, f"Erro de conexão: {e}"


    @staticmethod
    def remover_item_especifico(id_venda, id_item):
        try:
            resposta = requests.delete(f"{ServicoCarrinhoAPI.URL_BASE}/carrinho/{id_venda}/itens/{id_item}", timeout=5)
            return resposta.status_code, resposta.text
        except requests.exceptions.RequestException as e:
            return 500, f"Erro de conexão: {e}"

    @staticmethod
    def buscar_historico(id_cliente):
        try:
            resposta = requests.get(f"{ServicoCarrinhoAPI.URL_BASE}/cliente/{id_cliente}/historico", timeout=5)
            if resposta.status_code == 200:
                return resposta.json()
            elif resposta.status_code == 404:
                # Retorna lista vazia se o cliente ainda não tiver compras
                return []
            else:
                st.warning(f"Aviso ao buscar histórico: {resposta.text}")
                return []
        except requests.exceptions.RequestException as e:
            st.error(f"Erro ao buscar o histórico de compras. Detalhes: {e}")
            return []

class ServicoVendaAPI:
    URL_BASE = "https://localhost:8080"

    @staticmethod
    def buscar_vendas():
        try:
            resposta = requests.get(f"{ServicoVendaAPI.URL_BASE}/vendas", timeout=5)
            if resposta.status_code == 200:
                return resposta.json()
            elif resposta.status_code == 404:
                return []
            else:
                resposta.raise_for_status()
        except requests.exceptions.RequestException as e:
            st.error(f"Erro ao conectar com a API global de vendas. Detalhes: {e}")
            return []
        
    @staticmethod
    def buscar_relatorio_por_periodo(data_inicio, data_fim):
        
        parametros = {
            "inicio": data_inicio,
            "fim": data_fim
        }
        try:
            resposta = requests.get(f"{ServicoVendaAPI.URL_BASE}/vendas/relatorio", params=parametros, timeout=5)
            if resposta.status_code == 200:
                return resposta.json()
            elif resposta.status_code == 400:
                st.warning(f"Erro de validação do relatório: {resposta.text}")
                return []
            else:
                return []
        except requests.exceptions.RequestException as e:
            st.error(f"Erro ao conectar com a API de relatórios. Detalhes: {e}")
            return []
    
    @staticmethod
    def buscar_todos_itens_vendidos():
        try:
            resposta = requests.get(f"{ServicoVendaAPI.URL_BASE}/venda_itens", timeout=5)
            if resposta.status_code == 200:
                return resposta.json()
            elif resposta.status_code == 404:
                return []
            else:
                resposta.raise_for_status()
        except requests.exceptions.RequestException as e:
            st.error(f"Erro ao conectar com a API de listagem de itens vendidos. Detalhes: {e}")
            return []
    
    @staticmethod
    def alocar_entregador(id_venda, id_entregador):
        parametros = {"idEntregador": id_entregador}
        try:
            resposta = requests.put(f"{ServicoVendaAPI.URL_BASE}/vendas/{id_venda}/alocar", params=parametros, timeout=5)
            return resposta.status_code, resposta.text
        except requests.exceptions.RequestException as e:
            return 500, f"Erro de conexão ao alocar entregador: {e}"
        
    @staticmethod
    def finalizar_entrega(id_venda):
        try:
            resposta = requests.put(f"{ServicoVendaAPI.URL_BASE}/vendas/{id_venda}/finalizar-entrega", timeout=5)
            return resposta.status_code, resposta.text
        except requests.exceptions.RequestException as e:
            return 500, f"Erro de conexão ao finalizar entrega: {e}"