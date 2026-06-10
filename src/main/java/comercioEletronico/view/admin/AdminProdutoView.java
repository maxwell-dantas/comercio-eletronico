package comercioEletronico.view.admin;

import comercioEletronico.model.dao.VendaDao;
import comercioEletronico.model.dao.VendaItemDao;
import comercioEletronico.model.entities.Produto;
import comercioEletronico.model.dao.ProdutoDao;
import comercioEletronico.model.dao.CategoriaDao;
import comercioEletronico.model.entities.Venda;
import comercioEletronico.model.entities.VendaItem;

import java.util.ArrayList;

public class AdminProdutoView {
    private static ProdutoDao produtoDao = new ProdutoDao();
    private static CategoriaDao categoriaDao = new CategoriaDao();
    private static VendaDao vendaDao = new VendaDao();
    private static VendaItemDao vendaItemDao = new VendaItemDao();

    public static ArrayList<Produto> obterProdutos() {
        if (produtoDao.listar().isEmpty()) {
            throw new IllegalArgumentException("\nAinda não há nenhum produto cadastrado no sistema!");
        }
        return produtoDao.listar();
    }

    public static void inserir(String descricao, double preco, int estoque, int idCategoria, String imagemBase64) {
        if (categoriaDao.listar().isEmpty()) {
            throw new IllegalArgumentException("\nPrimeiro insira uma categoria no sistema antes de inserir um produto!");
        }
        if (categoriaDao.listarId(idCategoria) == null) {
            throw new IllegalArgumentException("\nEsta categoria não está cadastrada no sistema! Digite um ID válido!");
        }
        if (!produtoDao.isDescricaoDisponivel(descricao)) {
            throw new IllegalArgumentException("\nEste produto já está cadastrado no sistema.");
        }

        Produto produto = new Produto(descricao, preco, estoque, idCategoria, imagemBase64);
        produtoDao.inserir(produto);
    }

    public static Produto listarId(int id) {
        return produtoDao.listarId(id);
    }

    public static void atualizar(Produto produto, String descricao, double preco, int estoque, int idCategoria, String imagemBase64) {
        if (categoriaDao.listarId(idCategoria) == null) {
            throw new IllegalArgumentException("\nEsta categoria não está cadastrada no sistema! Digite um ID válido!");
        }
        if (!produto.getDescricao().equalsIgnoreCase(descricao) && !produtoDao.isDescricaoDisponivel(descricao)) {
            throw new IllegalArgumentException("\nEste produto já está cadastrado no sistema.");
        }
        produto.setDescricao(descricao);
        produto.setPreco(preco);
        produto.setEstoque(estoque);
        produto.setIdCategoria(idCategoria);
        produto.setImagemBase64(imagemBase64);
        produtoDao.atualizar(produto);
    }

    public static void remover(Produto produto) {
        // Impede apagar se estiver em compra FINALIZADA
        for (VendaItem item : vendaItemDao.listar()) {
            if (item.getIdProduto() == produto.getId()) {
                Venda venda = vendaDao.listarId(item.getIdVenda());

                if (venda != null && !venda.getCarrinho()) {
                    throw new IllegalArgumentException("Erro de validação: O produto não pode ser removido pois já faz parte de uma venda finalizada.");
                }
            }
        }
        
        // Remove o item de carrinhos ABERTOS antes de excluir o produto
        ArrayList<VendaItem> itensParaRemover = new ArrayList<>();
        for (VendaItem item : vendaItemDao.listar()) {
            if (item.getIdProduto() == produto.getId()) {
                itensParaRemover.add(item);
            }
        }
        
        for (VendaItem item : itensParaRemover) {
            vendaItemDao.remover(item.getId());
        }

        // Exclusão definitiva do produto
        produtoDao.remover(produto.getId());
    }

    public static void aplicarAumento(int idCategoria, double porcentagem) {
        if (porcentagem < 0.0) {
            throw new IllegalArgumentException("\nO valor do aumento precisa ser positivo.");
        }
        for (Produto produto : produtoDao.listar()) {
            if (produto.getIdCategoria() == idCategoria) {
                double novoPreco = produto.getPreco() * (1 + (porcentagem / 100.0));
                produto.setPreco(novoPreco);
                produtoDao.atualizar(produto);
            }
        }
    }
}