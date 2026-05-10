package comercioEletronico.template.cliente;

import comercioEletronico.model.entities.Venda;
import comercioEletronico.model.entities.VendaItem;
import comercioEletronico.view.Util;

import java.util.ArrayList;
import java.util.Scanner;

public class ClienteTemplate {
    private static Scanner scanner = new Scanner(System.in);

    public static int obterOpcaoMenu() {
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
        return Util.lerInteiroSeguro(scanner.nextLine());
    }

    public static String[] obterDados() {
        String[] dados = new String[2];
        System.out.print("\nDigite o ID do produto: ");
        dados[0] = scanner.nextLine();
        System.out.print("Digite a quantidade: ");
        dados[1] = scanner.nextLine();
        return dados;
    }

    public static void visualizarCarrinho(ArrayList<VendaItem> vendaItems, int idVenda) {
        System.out.println("\n=== SEU CARRINHO ===\n");
        boolean temItens = false;
        int contador = 1;
        double totalCarrinho = 0.0;

        for (VendaItem vendaItem : vendaItems) {
            if (vendaItem.getIdVenda() == idVenda) {
                System.out.println(contador + " - " + vendaItem + " - Total: R$ " + String.format("%.2f", vendaItem.getQuantidade() * vendaItem.getPreco()));
                totalCarrinho += vendaItem.getQuantidade() * vendaItem.getPreco();
                contador++;
                temItens = true;
            }
        }

        if (!temItens) {
            System.out.println("Seu carrinho está vazio.");
        } else {
            System.out.printf("\nTotal Carrinho: R$ %.2f\n", totalCarrinho);
        }
        System.out.println();
    }

    public static void listarCompras(ArrayList<Venda> listaVendas, int idCliente, ArrayList<VendaItem> vendaItems) {
        System.out.println("\n=== SEU HISTÓRICO DE COMPRAS ===\n");
        boolean temCompras = false;
        int contadorVenda = 1;

        for (Venda venda : listaVendas) {
            // lista apenas as vendas concluídas de um cliente específico e seus itens
            if (venda.getIdCliente() == idCliente && !venda.getCarrinho()) {
                System.out.println(contadorVenda + " - " + venda);
                contadorVenda++;

                int contadorItem = 1;
                for (VendaItem vendaItem : vendaItems) {
                    if (vendaItem.getIdVenda() == venda.getId()) {
                        System.out.println("    "+ contadorItem + " - " + vendaItem);
                        contadorItem++;
                    }
                }

                System.out.println();

                temCompras = true;
            }
        }

        if (!temCompras) {
            System.out.println("Você ainda não realizou nenhuma compra.");
        }
    }

    // MÉTODOS FEEDBACK

    public static void exibirSucessoAdicionado() {
        System.out.println("\nProduto adicionado ao carrinho!");
    }

    public static void exibirSucessoCompra() {
        System.out.println("\nCompra finalizada com sucesso! Agradecemos a preferência.\n");
    }

    public static void exibirMensagemCarrinhoLimpo() {
        System.out.println("\nSeu carrinho foi esvaziado.\n");
    }

    public static void exibirMensagemCarrinhoVazio() {
        System.out.println("\nNão é possível finalizar: Seu carrinho está vazio!");
    }

    public static void exibirErro(String erro) {
        System.out.println("\n" + erro);
    }

    public static void exibirErroProdutoNaoEncontrado() {
        System.out.println("\nEste produto não está cadastrado! Digite um ID válido.");
    }

    public static void exibirErroQuantidadeItens() {
        System.out.println("\nQuantidade indisponível no estoque no momento!");
    }

    public static void exibirErroOpcaoInvalida() {
        System.out.println("\nInsira um valor válido. Tente novamente!");
    }

    public static void exibirMensagemSaida() {
        System.out.println("\nSaindo da loja e voltando ao Menu Principal...");
    }

}