package comercioEletronico.template.admin;

import comercioEletronico.model.entities.Produto;
import comercioEletronico.util.Util;
import comercioEletronico.view.admin.AdminProdutoView;

import java.util.ArrayList;
import java.util.Scanner;

public class AdminProdutoTemplate {
    private static Scanner scanner = new Scanner(System.in);

    public static void menu() {
        int id;
        Produto produto;
        String descricao;
        double preco;
        int estoque;
        int idCategoria;

        int opcaoMenuAdminProduto = -1;
        while (opcaoMenuAdminProduto != 0) {
            System.out.println("""
                    
                    === CADASTRO DE PRODUTOS ===
                    
                    1. Listar Produtos
                    2. Inserir Produto
                    3. Atualizar Produto
                    4. Remover Produto
                    0. Sair
                    """);
            System.out.print("Digite uma opção: ");
            opcaoMenuAdminProduto = Util.lerInteiroSeguro(scanner.nextLine());

            try {
                switch (opcaoMenuAdminProduto) {
                    case 0:
                        System.out.println("\nVoltando ao Menu Principal...");
                        break;

                    case 1:
                        listarProdutos();
                        break;

                    case 2:
                        System.out.println("\n=== CADASTRAR PRODUTO ===\n");

                        System.out.print("Digite a descrição do produto: ");
                        descricao = scanner.nextLine();

                        System.out.print("Digite o preço do produto: ");
                        preco = Util.lerDoubleSeguro(scanner.nextLine());

                        System.out.print("Digite a quantidade em estoque do produto: ");
                        estoque = Util.lerInteiroSeguro(scanner.nextLine());

                        System.out.print("Digite o ID da categoria para este produto: ");
                        idCategoria = Util.lerInteiroSeguro(scanner.nextLine());

                        AdminProdutoView.inserir(descricao, preco, estoque, idCategoria);
                        System.out.println("\nProduto cadastrado com sucesso!\n");
                        break;

                    case 3:
                        AdminProdutoView.obterProdutos(); // retorna erro caso a lista seja vazia

                        System.out.println("\n=== ATUALIZAR PRODUTO ===\n");

                        System.out.print("Digite o ID do produto: ");
                        id = Util.lerInteiroSeguro(scanner.nextLine());

                        produto = AdminProdutoView.listarId(id);

                        if (produto == null) {
                            System.out.println("\nEste produto não está cadastrado no sistema! Digite um ID válido!");
                            continue;
                        }

                        System.out.print("Digite a nova descrição do produto: ");
                        descricao = scanner.nextLine();

                        System.out.print("Digite o novo preço do produto: ");
                        preco = Util.lerDoubleSeguro(scanner.nextLine());

                        System.out.print("Digite a nova quantidade em estoque do produto: ");
                        estoque = Util.lerInteiroSeguro(scanner.nextLine());

                        System.out.print("Digite o novo ID da categoria para este produto: ");
                        idCategoria = Util.lerInteiroSeguro(scanner.nextLine());

                        AdminProdutoView.atualizar(produto, descricao, preco, estoque, idCategoria);
                        System.out.println("\nProduto atualizado com sucesso!\n");
                        break;

                    case 4:
                        AdminProdutoView.obterProdutos(); // retorna erro caso a lista seja vazia

                        System.out.println("\n=== REMOVER PRODUTO ===\n");

                        System.out.print("Digite o ID do produto: ");
                        id = Util.lerInteiroSeguro(scanner.nextLine());

                        produto = AdminProdutoView.listarId(id);

                        if (produto == null) {
                            System.out.println("\nEste produto não está cadastrado no sistema! Digite um ID válido!");
                            continue;
                        }

                        AdminProdutoView.remover(produto);
                        System.out.println("\nProduto removido com sucesso!\n");
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

    public static void menuReajustePreco() {
        double porcentagem;

        int opcaoReajuste = -1;
        while (opcaoReajuste != 0) {
            System.out.println("""
                    
                    === REAJUSTE DE PREÇO PARA TODOS OS PRODUTOS ===
                    
                    1. Aumento
                    2. Desconto
                    3. Listar Produtos
                    0. Sair
                    """);
            System.out.print("Digite uma opção: ");
            opcaoReajuste = Util.lerInteiroSeguro(scanner.nextLine());

            try {
                switch (opcaoReajuste) {
                    case 0:
                        System.out.println("\nVoltando ao Menu Principal...");
                        break;

                    case 1:
                        AdminProdutoView.obterProdutos(); // retorna erro caso a lista seja vazia

                        System.out.print("\nEm quantos % você deseja aumentar o valor dos produtos? ");
                        porcentagem = Util.lerDoubleSeguro(scanner.nextLine());

                        AdminProdutoView.aplicarAumento(porcentagem);
                        System.out.println("\nAumento aplicado a todos os produtos com sucesso!\n");
                        break;

                    case 2:
                        AdminProdutoView.obterProdutos(); // retorna erro caso a lista seja vazia

                        System.out.print("\nEm quantos % você deseja dar desconto para o valor dos produtos? ");
                        porcentagem = Util.lerDoubleSeguro(scanner.nextLine());

                        AdminProdutoView.aplicarDesconto(porcentagem);
                        System.out.println("\nDesconto aplicado a todos os produtos com sucesso!\n");
                        break;

                    case 3:
                        listarProdutos();
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

    public static void listarProdutos() {
        try {
            ArrayList<Produto> listaProdutos = AdminProdutoView.obterProdutos();
            System.out.println("\n=== PRODUTOS CADASTRADOS ===\n");

            for (Produto produto : listaProdutos) {
                System.out.println(produto);
            }

            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}