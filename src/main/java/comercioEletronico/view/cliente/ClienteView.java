package comercioEletronico.view.cliente;

import comercioEletronico.model.dao.ProdutoDao;
import comercioEletronico.model.dao.VendaDao;
import comercioEletronico.model.dao.VendaItemDao;
import comercioEletronico.model.dao.PromocaoDao;
import comercioEletronico.model.entities.Produto;
import comercioEletronico.model.entities.Promocao;
import comercioEletronico.model.entities.Venda;
import comercioEletronico.model.entities.VendaItem;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ClienteView {
    private static ProdutoDao produtoDao = new ProdutoDao();
    private static VendaDao vendaDao = new VendaDao();
    private static VendaItemDao vendaItemDao = new VendaItemDao();
    private static PromocaoDao promocaoDao = new PromocaoDao();

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

    // Método dedicado para listar apenas as compras já finalizadas de um cliente específico
    public static ArrayList<Venda> obterHistoricoCompras(int idCliente) {
        ArrayList<Venda> historico = new ArrayList<>();

        for (Venda venda : vendaDao.listar()) {
            // Filtra pelo ID do cliente e garante que não é mais um carrinho em aberto
            if (venda.getIdCliente() == idCliente && !venda.getCarrinho()) {
                historico.add(venda);
            }
        }

        if (historico.isEmpty()) {
            throw new IllegalArgumentException("Você ainda não possui nenhuma compra finalizada.");
        }

        return historico;
    }

    public static void adicionarProduto(int idVenda, int idProduto, int quantidadeItems) {

        if (produtoDao.listar().isEmpty()) {
            throw new IllegalArgumentException("\nAinda não há nenhum produto cadastrado no sistema, sentimos muito!");
        }

        Produto produto = produtoDao.listarId(idProduto);

        if (produto == null) {
            throw new IllegalArgumentException("\nEste produto não está cadastrado! Digite um ID válido.");
        }
        
        // Calcula se há promoção ativa na categoria deste produto
        double precoFinal = produto.getPreco();
        LocalDate hoje = LocalDate.now();

        for (Promocao p : promocaoDao.listar()) {
            if (p.getIdCategoria() == produto.getIdCategoria()) {
                if (!hoje.isBefore(p.getDataInicio()) && !hoje.isAfter(p.getDataFim())) {
                    precoFinal = produto.getPreco() * (1 - (p.getPercentualDesconto() / 100.0));
                    break;
                }
            }
        }

        VendaItem vendaItem = vendaItemDao.obterVendaItemProduto(idVenda, idProduto);

        if (vendaItem != null) { // se o item já está no carrinho
            if (quantidadeItems + vendaItem.getQuantidade() > produto.getEstoque()) {
                throw new IllegalArgumentException("\nQuantidade indisponível no estoque no momento!");
            }

            vendaItem.setQuantidade(vendaItem.getQuantidade() + quantidadeItems);
            // Salva com o preço (com ou sem desconto) atualizado no momento
            vendaItem.setPreco(precoFinal); 
            vendaItemDao.atualizar(vendaItem);

        } else { // se o item é novo no carrinho
            if (quantidadeItems > produto.getEstoque()) {
                throw new IllegalArgumentException("\nQuantidade indisponível no estoque no momento!");
            }

            vendaItem = new VendaItem(quantidadeItems, precoFinal, idVenda, idProduto);
            vendaItemDao.inserir(vendaItem);
        }
    }

    // passa o idVenda para buscar apenas os itens do carrinho atual
    public static ArrayList<VendaItem> obterCarrinho(int idVenda) {
        ArrayList<VendaItem> itensDoCarrinho = new ArrayList<>();

        for (VendaItem item : vendaItemDao.listar()) {
            if (item.getIdVenda() == idVenda) {
                itensDoCarrinho.add(item);
            }
        }

        if (itensDoCarrinho.isEmpty()) {
            throw new IllegalArgumentException("\nSeu carrinho está vazio!");
        }

        return itensDoCarrinho;
    }

    public static void limparCarrinho(int idVenda) {
        vendaItemDao.limparCarrinho(idVenda);
    }

    public static void removerItemCarrinho(int idVenda, int idVendaItem) {
        VendaItem item = vendaItemDao.listarId(idVendaItem);

        if (item == null || item.getIdVenda() != idVenda) {
            throw new IllegalArgumentException("Erro de validação: Item não encontrado neste carrinho.");
        }

        vendaItemDao.remover(idVendaItem);
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

        Venda venda = vendaDao.listarId(idVenda);
        if (venda != null) {
            venda.setTotal(totalDaCompra);
            venda.setCarrinho(false);
            // Registra a data/hora exata do checkout para a logística e os relatórios de filtro do Admin
            venda.setData(LocalDateTime.now());
            vendaDao.atualizar(venda);
        }
    }
}