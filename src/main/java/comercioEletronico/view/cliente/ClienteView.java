package comercioEletronico.view.cliente;

import comercioEletronico.model.dao.ProdutoDao;
import comercioEletronico.model.dao.VendaDao;
import comercioEletronico.model.dao.VendaItemDao;
import comercioEletronico.model.entities.Produto;
import comercioEletronico.model.entities.Venda;
import comercioEletronico.model.entities.VendaItem;

import java.util.ArrayList;

public class ClienteView {
    private static ProdutoDao produtoDao = new ProdutoDao();
    private static VendaDao vendaDao = new VendaDao();
    private static VendaItemDao vendaItemDao = new VendaItemDao();

    public static void adicionarVenda(Venda venda) {
        vendaDao.inserir(venda);
    }

    public static Venda buscarCarrinhoAberto(int idCliente) {
        for (Venda venda : vendaDao.listar()) {
            if (venda.getIdCliente() == idCliente && venda.getCarrinho()) {
                return venda;
            }
        }
        return null;
    }

    public static void adicionarProduto(int idVenda, int idProduto, int quantidadeItems) {

        if (produtoDao.listar().isEmpty()) {
            throw new IllegalArgumentException("\nAinda não há nenhum produto cadastrado no sistema, sentimos muito!");
        }

        Produto produto = produtoDao.listarId(idProduto); // retorna um produto

        if (produto == null) {
            throw new IllegalArgumentException("\nEste produto não está cadastrado! Digite um ID válido.");
        }

        // retorna null caso o item ainda não esteja no carrinho
        VendaItem vendaItem = vendaItemDao.obterVendaItemProduto(idVenda, idProduto);

        if (vendaItem != null) { // se o item já está no carrinho

            // verifica se a quantidade adicionada ultrapassa o valor do estoque
            if (quantidadeItems + vendaItem.getQuantidade() > produto.getEstoque()) {
                throw new IllegalArgumentException("\nQuantidade indisponível no estoque no momento!");
            }

            vendaItemDao.atualizar(vendaItem.getId(), (vendaItem.getQuantidade() + quantidadeItems), produto.getPreco());

        } else { // se o item é novo no carrinho

            // verifica se a quantidade pedida ultrapassa o valor do estoque
            if (quantidadeItems > produto.getEstoque()) {
                throw new IllegalArgumentException("\nQuantidade indisponível no estoque no momento!");
            }

            vendaItem = new VendaItem(quantidadeItems, produto.getPreco(), idVenda, idProduto);
            vendaItemDao.inserir(vendaItem);
        }
    }

    public static ArrayList<VendaItem> obterCarrinho() {
        if (vendaItemDao.listar().isEmpty()) {
            throw new IllegalArgumentException("\nAinda não há nenhuma venda cadastrada no sistema!");
        }
        return vendaItemDao.listar();
    }

    public static void limparCarrinho(int idVenda) {
        vendaItemDao.limparCarrinho(idVenda);
    }

    public static void finalizarCompra(int idVenda) {
        boolean temItens = false;
        double totalDaCompra = 0.0;

        for (VendaItem item : vendaItemDao.listar()) {
            if (item.getIdVenda() == idVenda) {
                temItens = true;
                totalDaCompra += (item.getPreco() * item.getQuantidade()); // cacula preço x quantidade e soma ao total
                produtoDao.atualizarEstoque(item.getIdProduto(), item.getQuantidade()); // baixa no estoque
            }
        }

        if (!temItens) {
            throw new IllegalArgumentException("\nNão é possível finalizar: Seu carrinho está vazio!");
        }

        // fecha o carrinho e passa o total da compra
        vendaDao.atualizar(idVenda, false, totalDaCompra);
    }
}