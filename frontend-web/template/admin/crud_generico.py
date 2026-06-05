from abc import ABC, abstractmethod
import streamlit as st
import requests
import time

class CrudGenerico(ABC):
    def __init__(self, titulo_pagina, tipo_entidade, endpoint_api):
        self._titulo = titulo_pagina
        self._entidade = tipo_entidade
        self._rota = f"http://localhost:8080/{endpoint_api}"

    @abstractmethod
    def formatar_selectbox(self, content):
        pass

    @abstractmethod
    def inserir(self):
        pass

    @abstractmethod
    def atualizar(self, item_selecionado):
        pass

    def menu(self):
        st.title(self._titulo)
        aba_listar, aba_inserir, aba_atualizar, aba_deletar = st.tabs(["Listar", "Inserir", "Atualizar", "Deletar"])

        # LISTAR
        with aba_listar:
            st.subheader(f"Registro de {self._entidade.capitalize()}")

            resposta_listar = requests.get(self._rota) # requisita arquivo .json da rota
            resposta_listar.encoding = "utf-8"

            if resposta_listar.status_code == 200:
                st.dataframe(resposta_listar.json(), use_container_width=True) # transforma o arquivo .json em uma tabela
            else:
                st.warning(resposta_listar.text)

        # INSERIR
        with aba_inserir:
            st.subheader(f"Cadastrar {self._entidade.capitalize()}")

            with st.form("cadastro"):
                dados_cadastro = self.inserir() # obtem dados em formato .json

                if st.form_submit_button("Cadastrar"):

                    resposta_inserir = requests.post(self._rota, json=dados_cadastro)
                    resposta_inserir.encoding = "utf-8"

                    if resposta_inserir.status_code == 201:
                        st.success(resposta_inserir.text)
                        time.sleep(2)
                        st.rerun()
                    else:
                        st.error(resposta_inserir.text)

        # ATUALIZAR
        with aba_atualizar:
            st.subheader(f"Atualização de {self._entidade.capitalize()}")

            resposta_atualizar = requests.get(self._rota) # requisita arquivo .json da rota
            resposta_atualizar.encoding = "utf-8"

            if resposta_atualizar.status_code == 200:

                entidade_selecionada = st.selectbox(
                    f"Seleção de {self._entidade.capitalize()} para alterar:",
                    options=resposta_atualizar.json(),
                    format_func=self.formatar_selectbox,
                    key="select_atualizar"
                )

                with st.form("atualizar"):
                    st.write("Edite os dados abaixo:")
                    dados_atualizados = self.atualizar(entidade_selecionada)

                    if st.form_submit_button("Atualizar"):
                        resposta_up = requests.put(f"{self._rota}/{entidade_selecionada['id']}", json=dados_atualizados)
                        resposta_up.encoding = "utf-8"

                        if resposta_up.status_code == 200:
                            st.success(resposta_up.text)
                            time.sleep(2)
                            st.rerun() # recarrega a página para atualizar a lista do selectbox e a tabela
                        else:
                            st.error(resposta_up.text)
            else:
                st.warning(resposta_atualizar.text)

        # DELETAR
        with aba_deletar:
            st.subheader(f"Remover {self._entidade.capitalize()} do Sistema")

            resposta_deletar = requests.get(self._rota) # requisita arquivo .json da rota
            resposta_deletar.encoding = "utf-8"

            if resposta_deletar.status_code == 200:

                with st.container(border=True):
                    entidade_selecionada = st.selectbox(
                        f"Seleção de {self._entidade.capitalize()} para remover:",
                        options=resposta_deletar.json(),
                        format_func=self.formatar_selectbox,
                        key="select_deletar"
                    )

                    st.warning(f"⚠️ Atenção: Esta ação apagará permanentemente o dado: {self.formatar_selectbox(entidade_selecionada)}.")

                    if st.button("Deletar"):
                        resposta_del = requests.delete(f"{self._rota}/{entidade_selecionada['id']}")
                        resposta_del.encoding = "utf-8"

                        if resposta_del.status_code == 200:
                            st.success(resposta_del.text)
                            time.sleep(2)
                            st.rerun()
                        else:
                            st.error(resposta_del.text)
            else:
                st.warning(resposta_deletar.text)