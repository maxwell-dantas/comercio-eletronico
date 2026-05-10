package comercioEletronico.model.dao;

import comercioEletronico.model.entities.VendaItem;

import java.util.ArrayList;
import java.lang.reflect.Type;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class VendaItemDao {
    private static ArrayList<VendaItem> listaVendaItens = new ArrayList<>();
    private static final Gson gson = new Gson();

    public void inserir(VendaItem vendaItem) {
        abrir();
        int idGenerator = 1;
        if (!listaVendaItens.isEmpty()) {
            idGenerator = listaVendaItens.getLast().getId() + 1;
        }
        vendaItem.setId(idGenerator);
        listaVendaItens.add(vendaItem);
        salvar();
    }

    public VendaItem listarId(int id) {
        abrir();
        for (VendaItem vendaItem : listaVendaItens) {
            if (vendaItem.getId() == id) {
                return vendaItem;
            }
        }
        return null;
    }

    public void atualizar(int id, int quantidade, double preco) {
        VendaItem vendaItem = listarId(id);
        if (vendaItem != null) {
            vendaItem.setQuantidade(quantidade);
            vendaItem.setPreco(preco);
            salvar();
        }
    }

    public void remover(int id) {
        VendaItem vendaItem = listarId(id);
        if (vendaItem != null) {
            listaVendaItens.remove(vendaItem);
            salvar();
        }
    }

    public ArrayList<VendaItem> listar() {
        abrir();
        return listaVendaItens;
    }

    public void limparCarrinho(int idVenda) {
        abrir();
        listaVendaItens.removeIf(item -> item.getIdVenda() == idVenda);
        salvar();
    }

    public VendaItem obterVendaItemProduto(int idVenda, int idProduto) {
        abrir();
        for (VendaItem item : listaVendaItens) {
            // verifica se o item pertence ao carrinho atual (idVenda) e é o produto que o cliente está tentando adicionar
            if (item.getIdVenda() == idVenda && item.getIdProduto() == idProduto) {
                return item;
            }
        }
        return null;
    }

    public void abrir() {
        try {
            // 1. Abre o leitor para o caminho onde o arquivo foi salvo
            FileReader leitor = new FileReader("src/comercioEletronico/data/venda_itens.json");

            // 2. Verifica o tipo da lista
            Type listaTipo = new TypeToken<ArrayList<VendaItem>>() {
            }.getType();

            listaVendaItens = gson.fromJson(leitor, listaTipo); // 3. Leitura de arquivo

            if (listaVendaItens == null) { // camada de segurança extra (caso o usuário não insira nada)
                listaVendaItens = new ArrayList<>();
            }

            leitor.close();  // 4. fecha o leitor

        } catch (IOException e) {
            // Caso o arquivo não exista ainda, cria uma lista vazia para evitar erros (o método gson.fromJson)
            listaVendaItens = new ArrayList<>();
        }
    }

    public void salvar() {
        try {
            // 1. Abertura de arquivo
            FileWriter escritor = new FileWriter("src/comercioEletronico/data/venda_itens.json");

            // 2. Converte a lista de objetos em texto Json
            String textoJson = gson.toJson(listaVendaItens);

            // 3. Escreve o texto no arquivo
            escritor.write(textoJson);

            // 4. Fecha o arquivo para confirmar a gravação
            escritor.close();

        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao salvar o arquivo: " + e.getMessage());
        }
    }
}