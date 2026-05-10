package comercioEletronico.view.cliente;

import comercioEletronico.model.dao.ProdutoDao;
import comercioEletronico.model.dao.VendaDao;
import comercioEletronico.model.dao.VendaItemDao;
import comercioEletronico.model.entities.Produto;
import comercioEletronico.model.entities.Venda;
import comercioEletronico.model.entities.VendaItem;
import comercioEletronico.template.admin.AdminProdutoTemplate;
import comercioEletronico.template.cliente.ClienteTemplate;
import comercioEletronico.view.Util;

public class ClienteView {
    private ProdutoDao produtoDao = new ProdutoDao();
    private VendaDao vendaDao = new VendaDao();
    private VendaItemDao vendaItemDao = new VendaItemDao();

    public void exibirMenu(int idClienteLogado) {
        Venda venda = new Venda(idClienteLogado);
        vendaDao.inserir(venda);
        int idVenda = venda.getId();

        VendaItem vendaItem;
        Produto produto;
        int idProduto;
        int quantidadeItens;
        String[] dados;
        int opcaoMenu;

        do {
            opcaoMenu = ClienteTemplate.obterOpcaoMenu();

            switch (opcaoMenu) {
                case 0:
                    vendaItemDao.limparCarrinho(idVenda);
                    ClienteTemplate.exibirMensagemSaida();
                    break;

                case 1:
                    AdminProdutoTemplate.listarProdutos(produtoDao.listar());
                    Util.pausar();
                    break;

                case 2:
                    dados = ClienteTemplate.obterDados();

                    try {
                        idProduto = Integer.parseInt(dados[0]);
                        quantidadeItens = Integer.parseInt(dados[1]);
                    } catch (NumberFormatException e) {
                        ClienteTemplate.exibirErro("Formatação inválida: Digite apenas números.");
                        continue;
                    }

                    produto = produtoDao.listarId(idProduto);

                    if (produto == null) {
                        ClienteTemplate.exibirErroProdutoNaoEncontrado();
                        continue;
                    }

                    vendaItem = vendaItemDao.obterVendaItemProduto(idVenda, idProduto);

                    if (vendaItem != null) { // Se o item já está no carrinho
                        if (quantidadeItens + vendaItem.getQuantidade() > produto.getEstoque()) {
                            ClienteTemplate.exibirErroQuantidadeItens();
                            continue;
                        }

                        vendaItemDao.atualizar(vendaItem.getId(), (vendaItem.getQuantidade() + quantidadeItens), produto.getPreco());
                        ClienteTemplate.exibirSucessoAdicionado();
                        continue;
                    }

                    // Se o item é novo no carrinho
                    if (quantidadeItens > produto.getEstoque()) {
                        ClienteTemplate.exibirErroQuantidadeItens();
                        continue;
                    }

                    vendaItem = new VendaItem(quantidadeItens, produto.getPreco(), idVenda, idProduto);
                    vendaItemDao.inserir(vendaItem);
                    ClienteTemplate.exibirSucessoAdicionado();
                    break;

                case 3:
                    ClienteTemplate.visualizarCarrinho(vendaItemDao.listar(), idVenda);
                    Util.pausar();
                    break;

                case 4:
                    vendaItemDao.limparCarrinho(idVenda);
                    ClienteTemplate.exibirMensagemCarrinhoLimpo();
                    Util.pausar();
                    break;

                case 5:
                    boolean temItens = false;
                    double totalDaCompra = 0.0;

                    for (VendaItem item : vendaItemDao.listar()) {
                        if (item.getIdVenda() == idVenda) {
                            temItens = true;
                            // cacula preço x quantidade e soma ao total
                            totalDaCompra += (item.getPreco() * item.getQuantidade());

                            // baixa no estoque
                            produtoDao.atualizarEstoque(item.getIdProduto(), item.getQuantidade());
                        }
                    }

                    if (!temItens) {
                        ClienteTemplate.exibirMensagemCarrinhoVazio();
                        continue;
                    }

                    // fecha o carrinho e passa o total da compra
                    vendaDao.atualizar(idVenda, false, totalDaCompra);
                    ClienteTemplate.exibirSucessoCompra();

                    // prepara um novo carrinho caso o cliente queira continuar comprando sem precisar deslogar e logar novamente
                    venda = new Venda(idClienteLogado);
                    vendaDao.inserir(venda);
                    idVenda = venda.getId();

                    Util.pausar();
                    break;

                case 6:
                    ClienteTemplate.listarCompras(vendaDao.listar(), idClienteLogado);
                    Util.pausar();
                    break;

                default:
                    ClienteTemplate.exibirErroOpcaoInvalida();
                    break;
            }

        } while (opcaoMenu != 0);
    }
}