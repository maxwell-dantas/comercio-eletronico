from abc import ABC, abstractmethod
import streamlit as st
import requests
import time
import pandas as pd

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

    def get_abas_extras(self):
        """Retorna uma lista com os nomes de abas adicionais."""
        return []

    def renderizar_abas_extras(self, abas_extras):
        """Recebe os objetos das abas extras para desenhar o conteúdo."""
        pass

    def formatar_dataframe(self, df):
        """Permite que as classes filhas manipulem e formatem as colunas do DataFrame antes de exibir."""
        return df

    def menu(self):
        st.title(self._titulo)
        
        # Cria as abas de forma dinâmica
        nomes_abas_padrao = ["Listar", "Inserir", "Atualizar", "Deletar"]
        nomes_abas_extras = self.get_abas_extras()
        
        todas_as_abas = st.tabs(nomes_abas_padrao + nomes_abas_extras)

        aba_listar = todas_as_abas[0]
        aba_inserir = todas_as_abas[1]
        aba_atualizar = todas_as_abas[2]
        aba_deletar = todas_as_abas[3]

        # LISTAR
        with aba_listar:
            st.subheader(f"Registro de {self._entidade.capitalize()}")

            resposta_listar = requests.get(self._rota)
            resposta_listar.encoding = "utf-8"

            if resposta_listar.status_code == 200:
                dados = resposta_listar.json()
                
                if dados:
                    df = pd.DataFrame(dados)
                    
                    # CHAMA O HOOK PARA FORMATAR A TABELA
                    df = self.formatar_dataframe(df)
                    
                    configuracoes_colunas = {}

                    if "imagemBase64" in df.columns:
                        df["imagemBase64"] = df["imagemBase64"].apply(
                            lambda img: f"data:image/jpeg;base64,{img}" if img else None
                        )
                        configuracoes_colunas["imagemBase64"] = st.column_config.ImageColumn(
                            "Imagem", 
                            help="Miniatura do produto"
                        )

                    st.dataframe(
                        df, 
                        column_config=configuracoes_colunas, 
                        use_container_width=True,
                        hide_index=True
                    )
                else:
                    st.info(f"Nenhum registro de {self._entidade} encontrado.")
            else:
                st.warning(resposta_listar.text)

        # INSERIR
        with aba_inserir:
            st.subheader(f"Cadastrar {self._entidade.capitalize()}")

            with st.form("cadastro"):
                dados_cadastro = self.inserir()

                if st.form_submit_button("Cadastrar"):
                    if dados_cadastro:
                        resposta_inserir = requests.post(self._rota, json=dados_cadastro)
                        resposta_inserir.encoding = "utf-8"

                        if resposta_inserir.status_code == 201:
                            st.success(resposta_inserir.text)
                            time.sleep(2)
                            st.rerun()
                        else:
                            st.error(resposta_inserir.text)
                    else:
                        st.error("Ação bloqueada: resolva as pendências acima antes de cadastrar.")

        # ATUALIZAR
        with aba_atualizar:
            st.subheader(f"Atualização de {self._entidade.capitalize()}")

            resposta_atualizar = requests.get(self._rota)
            resposta_atualizar.encoding = "utf-8"

            if resposta_atualizar.status_code == 200:
                lista_atualizar = resposta_atualizar.json()
                
                if lista_atualizar:
                    entidade_selecionada = st.selectbox(
                        f"Seleção de {self._entidade.capitalize()} para alterar:",
                        options=lista_atualizar,
                        format_func=self.formatar_selectbox,
                        key="select_atualizar"
                    )

                    with st.form("atualizar"):
                        st.write("Edite os dados abaixo:")
                        dados_atualizados = self.atualizar(entidade_selecionada)

                        if st.form_submit_button("Atualizar"):
                            if dados_atualizados:
                                resposta_up = requests.put(f"{self._rota}/{entidade_selecionada['id']}", json=dados_atualizados)
                                resposta_up.encoding = "utf-8"

                                if resposta_up.status_code == 200:
                                    st.success(resposta_up.text)
                                    time.sleep(2)
                                    st.rerun()
                                else:
                                    st.error(resposta_up.text)
                            else:
                                st.error("Ação bloqueada: resolva as pendências acima antes de atualizar.")
                else:
                    st.info(f"Não há {self._entidade}s cadastrados para atualizar.")
            else:
                st.warning(resposta_atualizar.text)

        # DELETAR
        with aba_deletar:
            st.subheader(f"Remover {self._entidade.capitalize()} do Sistema")

            resposta_deletar = requests.get(self._rota)
            resposta_deletar.encoding = "utf-8"

            if resposta_deletar.status_code == 200:
                lista_deletar = resposta_deletar.json()
                
                if lista_deletar:
                    with st.container(border=True):
                        entidade_selecionada = st.selectbox(
                            f"Seleção de {self._entidade.capitalize()} para remover:",
                            options=lista_deletar,
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
                    st.info(f"Não há {self._entidade}s cadastrados para deletar.")
            else:
                st.warning(resposta_deletar.text)

        # RENDERIZAÇÃO DAS ABAS EXTRAS
        if nomes_abas_extras:
            self.renderizar_abas_extras(todas_as_abas[4:])