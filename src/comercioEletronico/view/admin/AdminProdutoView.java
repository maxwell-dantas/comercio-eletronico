package comercioEletronico.view.admin;

import comercioEletronico.model.entities.Produto;
import comercioEletronico.model.dao.ProdutoDao;
import comercioEletronico.model.dao.CategoriaDao;

import java.util.ArrayList;

public class AdminProdutoView {
    private static ProdutoDao produtoDao = new ProdutoDao();
    private static CategoriaDao categoriaDao = new CategoriaDao();

    public static ArrayList<Produto> obterProdutos() {
        if (produtoDao.listar().isEmpty()) {
            throw new IllegalArgumentException("\nAinda não há nenhum produto cadastrado no sistema!\n");
        }
        return produtoDao.listar();
    }

    public static void inserir(String descricao, double preco, int estoque, int idCategoria) {
        if (categoriaDao.listar().isEmpty()) {
            throw new IllegalArgumentException("\nPrimeiro insira uma categoria no sistema antes de inserir um produto!\n");
        }
        if (categoriaDao.listarId(idCategoria) == null) {
            throw new IllegalArgumentException("\nEsta categoria não está cadastrada no sistema! Digite um ID válido!\n");
        }
        if (!produtoDao.isDescricaoDisponivel(descricao)) {
            throw new IllegalArgumentException("\nEste produto já está cadastrado no sistema.\n");
        }

        Produto produto = new Produto(descricao, preco, estoque, idCategoria);
        produtoDao.inserir(produto);
    }

    public static Produto listarId(int id) {
        return produtoDao.listarId(id);
    }

    public static void atualizar(Produto produto, String descricao, double preco, int estoque, int idCategoria) {
        if (categoriaDao.listarId(idCategoria) == null) {
            throw new IllegalArgumentException("\nEsta categoria não está cadastrada no sistema! Digite um ID válido!\n");
        }
        if (!produto.getDescricao().equalsIgnoreCase(descricao) && !produtoDao.isDescricaoDisponivel(descricao)) {
            throw new IllegalArgumentException("\nEste produto já está cadastrado no sistema.\n");
        }

        produtoDao.atualizar(produto.getId(), descricao, preco, estoque, idCategoria);
    }

    public static void remover(Produto produto) {
        produtoDao.remover(produto.getId());
    }

    public static void aplicarAumento(double porcentagem) {
        if (porcentagem < 0.0) {
            throw new IllegalArgumentException("\nO valor do aumento precisa ser positivo.\n");
        }
        for (Produto produto : produtoDao.listar()) {
            double novoPreco = produto.getPreco() * (1 + (porcentagem / 100.0));
            produtoDao.atualizar(produto.getId(), produto.getDescricao(), novoPreco, produto.getEstoque(), produto.getIdCategoria());
        }
    }

    public static void aplicarDesconto(double porcentagem) {
        if (porcentagem < 0.0 || porcentagem > 99.99) {
            throw new IllegalArgumentException("\nO valor do desconto precisa estar entre 0 e 99.99%.\n");
        }
        for (Produto produto : produtoDao.listar()) {
            double novoPreco = produto.getPreco() * (1 - (porcentagem / 100.0));
            produtoDao.atualizar(produto.getId(), produto.getDescricao(), novoPreco, produto.getEstoque(), produto.getIdCategoria());
        }
    }
}