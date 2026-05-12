package comercioEletronico.model.dao;

import comercioEletronico.model.entities.Produto;

import java.util.ArrayList;
import java.lang.reflect.Type;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ProdutoDao {
    private static ArrayList<Produto> listaProdutos = new ArrayList<>();
    private static final Gson gson = new Gson();

    public void inserir(Produto produto) {
        abrir();
        int idGenerator = 1;
        if (!listaProdutos.isEmpty()) {
            idGenerator = listaProdutos.getLast().getId() + 1;
        }
        produto.setId(idGenerator);
        listaProdutos.add(produto);
        salvar();
    }

    public Produto listarId(int id) {
        abrir();
        for (Produto produto : listaProdutos) {
            if (produto.getId() == id) {
                return produto;
            }
        }
        return null;
    }

    public void atualizar(int id, String descricao, double preco, int estoque, int idCategoria) {
        Produto produto = listarId(id);
        if (produto != null) {
            produto.setDescricao(descricao);
            produto.setPreco(preco);
            produto.setEstoque(estoque);
            produto.setIdCategoria(idCategoria);
            salvar();
        }
    }

    public boolean isDescricaoDisponivel(String descricao) {
        abrir();
        for (Produto produto : listaProdutos) {
            if (produto.getDescricao().equalsIgnoreCase(descricao)) {
                return false;
            }
        }
        return true;
    }

    public void atualizarEstoque(int idProduto, int estoque) {
        Produto produto = listarId(idProduto);
        if (produto!= null) {
            produto.setEstoque(produto.getEstoque() - estoque);
            salvar();
        }
    }

    public void remover(int id) {
        Produto produto = listarId(id);
        if (produto != null) {
            listaProdutos.remove(produto);
            salvar();
        }
    }

    public ArrayList<Produto> listar() {
        abrir();
        return listaProdutos;
    }

    public void abrir() {
        try {
            // 1. Abre o leitor para o caminho onde o arquivo foi salvo
            FileReader leitor = new FileReader("src/comercioEletronico/data/produtos.json");

            // 2. Verifica o tipo da lista
            Type listaTipo = new TypeToken<ArrayList<Produto>>() {
            }.getType();

            listaProdutos = gson.fromJson(leitor, listaTipo); // 3. Leitura de arquivo

            if (listaProdutos == null) { // camada de segurança extra (caso o usuário não insira nada)
                listaProdutos = new ArrayList<>();
            }

            leitor.close();  // 4. fecha o leitor

        } catch (IOException e) {
            // Caso o arquivo não exista ainda, cria uma lista vazia para evitar erros (o método gson.fromJson)
            listaProdutos = new ArrayList<>();
        }
    }

    public void salvar() {
        try {
            // 1. Abertura de arquivo
            FileWriter escritor = new FileWriter("src/comercioEletronico/data/produtos.json");

            // 2. Converte a lista de objetos em texto Json
            String textoJson = gson.toJson(listaProdutos);

            // 3. Escreve o texto no arquivo
            escritor.write(textoJson);

            // 4. Fecha o arquivo para confirmar a gravação
            escritor.close();

        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao salvar o arquivo: " + e.getMessage());
        }
    }
}