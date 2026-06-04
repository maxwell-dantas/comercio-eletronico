package comercioEletronico.template.cliente;

import comercioEletronico.model.entities.Venda;
import comercioEletronico.model.entities.VendaItem;
import comercioEletronico.template.admin.AdminProdutoTemplate;
import comercioEletronico.view.admin.AdminCategoriaView;
import comercioEletronico.view.admin.AdminProdutoView;
import comercioEletronico.view.admin.AdminView;
import comercioEletronico.view.cliente.ClienteView;
import comercioEletronico.util.Util;

import java.util.ArrayList;
import java.util.Scanner;

public class ClienteTemplate {
    private static Scanner scanner = new Scanner(System.in);

    public static void menu(int idCliente) {
        Venda venda = ClienteView.buscarCarrinhoAberto(idCliente);

        if (venda == null) {
            venda = new Venda(idCliente);
            ClienteView.adicionarVenda(venda);
        }

        int opcaoMenuCliente = -1;
        while (opcaoMenuCliente != 0) {
            System.out.println("""
                    
                    === ÁREA DO CLIENTE ===
                    
                    1. Listar Produtos
                    2. Adicionar Produto ao Carrinho
                    3. Visualizar Carrinho
                    4. Limpar Carrinho
                    5. Finalizar Compra
                    6. Histórico de Compras
                    0. Sair
                    """);
            System.out.print("Digite uma opção: ");
            opcaoMenuCliente = Util.lerInteiroSeguro(scanner.nextLine());

            try {
                switch (opcaoMenuCliente) {
                    case 0:
                        System.out.println("\nSaindo da loja e voltando ao Menu Principal...");
                        break;

                    case 1:
                        AdminProdutoTemplate.listarProdutos();
                        System.out.println();
                        Util.pausar();
                        break;

                    case 2:
                        AdminProdutoView.obterProdutos();
                        System.out.print("\nDigite o ID do produto: ");
                        int idProduto = Util.lerInteiroSeguro(scanner.nextLine());

                        System.out.print("Digite a quantidade: ");
                        int quantidadeItems = Util.lerInteiroSeguro(scanner.nextLine());

                        ClienteView.adicionarProduto(venda.getId(), idProduto, quantidadeItems);
                        System.out.println("\nProduto adicionado ao carrinho!");
                        break;

                    case 3:
                        visualizarCarrinho(venda.getId());
                        System.out.println();
                        Util.pausar();
                        break;

                    case 4:
                        ClienteView.limparCarrinho(venda.getId());
                        System.out.println("\nSeu carrinho foi esvaziado.");
                        break;

                    case 5:
                        ClienteView.finalizarCompra(venda.getId());
                        System.out.println("\nCompra finalizada com sucesso! Agradecemos a preferência.");

                        venda = new Venda(idCliente);
                        ClienteView.adicionarVenda(venda);
                        break;

                    case 6:
                        listarCompras(idCliente);
                        Util.pausar();
                        break;

                    default:
                        System.out.println("\nInsira um valor válido. Tente novamente!");
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void visualizarCarrinho(int idVenda) {
        try { // caso não existam vendas registradas evita o programa de gastar processamento (obs.: vendas geral, ainda não esta implementado apenas para o cliente)
            ArrayList<VendaItem> vendaItems = ClienteView.obterCarrinho(idVenda);

            System.out.println("\n=== SEU CARRINHO ===\n");

            int contador = 1;
            double totalCarrinho = 0.0;

            for (VendaItem vendaItem : vendaItems) {
                System.out.println(contador + " - " + AdminProdutoView.listarId(vendaItem.getIdProduto()).getDescricao() + " - " + vendaItem
                        + " - Total: R$ " + String.format("%.2f", vendaItem.getQuantidade() * vendaItem.getPreco()));
                totalCarrinho += vendaItem.getQuantidade() * vendaItem.getPreco();
                contador++;
            }


            System.out.printf("\nTotal Carrinho: R$ %.2f\n", totalCarrinho);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void listarCompras(int idCliente) {
        try {
            ArrayList<Venda> listaVendas = AdminView.obterVendas();
            ArrayList<VendaItem> vendaItems = AdminView.obterVendaItems();

            System.out.println("\n=== SEU HISTÓRICO DE COMPRAS ===\n");

            int contadorVenda = 1;

            for (Venda venda : listaVendas) {
                // lista apenas as vendas concluídas de um cliente específico e seus itens
                if (venda.getIdCliente() == idCliente && !venda.getCarrinho()) { // carrinho falso = venda concluída
                    System.out.println(contadorVenda + " - " + venda);
                    contadorVenda++;

                    int contadorItem = 1;
                    for (VendaItem vendaItem : vendaItems) {
                        if (vendaItem.getIdVenda() == venda.getId()) {
                            System.out.println("    " + contadorItem + " - " + AdminProdutoView.listarId(vendaItem.getIdProduto()).getDescricao() + " - "
                                    + vendaItem + " - Categoria: " + AdminCategoriaView.listarId(AdminProdutoView.listarId(vendaItem.getIdProduto()).getIdCategoria()).getDescricao());
                            contadorItem++;
                        }
                    }

                    System.out.println();


                }
            }


        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}