package comercioEletronico.template.admin;

import comercioEletronico.model.entities.Produto;
import comercioEletronico.view.Util;

import java.util.ArrayList;
import java.util.Scanner;

public class AdminProdutoTemplate {
    private static Scanner scanner = new Scanner(System.in);

    public static int obterOpcaoCrud() {
        System.out.println("""
                
                === CADASTRO DE PRODUTOS ===
                
                1. Listar Produtos
                2. Inserir Produto
                3. Atualizar Produto
                4. Remover Produto
                0. Sair
                """);
        System.out.print("Digite uma opção: ");
        return Util.lerInteiroSeguro(scanner.nextLine());
    }

    public static int obterOpcaoReajustarPreco() {
        System.out.println("""
                
                === REAJUSTE DE PREÇO PARA TODOS OS PRODUTOS ===
                
                1. Aumento
                2. Desconto
                0. Sair
                """);
        System.out.print("Digite uma opção: ");
        return Util.lerInteiroSeguro(scanner.nextLine());
    }

    public static double obterPorcentagem(int opcaoReajuste) {
        if (opcaoReajuste == 1) {
            System.out.print("\nEm quantos % você deseja aumentar o valor dos produtos? ");
            return Util.lerDoubleSeguro(scanner.nextLine());
        }
        System.out.print("\nEm quantos % você deseja dar desconto para o valor dos produtos? ");
        return Util.lerDoubleSeguro(scanner.nextLine());
    }

    public static int obterId() {
        System.out.print("Digite o ID do produto: ");
        return Util.lerInteiroSeguro(scanner.nextLine());
    }

    public static String[] obterDados() {
        String[] dados = new String[4];

        System.out.print("Digite a descrição do produto: ");
        dados[0] = scanner.nextLine();

        System.out.print("Digite o preço do produto: ");
        dados[1] = scanner.nextLine();

        System.out.print("Digite a quantidade em estoque do produto: ");
        dados[2] = scanner.nextLine();

        System.out.print("Digite o ID da categoria para este produto: ");
        dados[3] = scanner.nextLine();

        return dados;
    }

    public static void listarProdutos(ArrayList<Produto> listaProdutos) {
        System.out.println("\n=== PRODUTOS CADASTRADOS ===\n");
        for (Produto produto : listaProdutos) {
            System.out.println(produto);
        }
        System.out.println();
    }

    // MÉTODOS DE FEEDBACK

    public static void exibirMensagemSaida() {
        System.out.println("\nVoltando ao Menu Principal...");
    }

    public static void exibirMensagemListaVazia() {
        System.out.println("\nAinda não há nenhum produto cadastrado na base de dados!");
    }

    public static void exibirCabecalho(String operacao) {
        System.out.println("\n=== " + operacao.toUpperCase() + " DE PRODUTO ===\n");
    }

    public static void exibirSucesso(String acao) {
        System.out.println("\nProduto " + acao + " com sucesso!\n");
    }

    public static void exibirSucessoReajuste(int opcao) {
        if (opcao == 1) {
            System.out.println("\nAumento aplicado a todos os produtos com sucesso!\n");
        } else {
            System.out.println("\nDesconto aplicado a todos os produtos com sucesso!\n");
        }
    }

    public static void exibirErro(String erro) {
        System.out.println("\n" + erro);
    }

    public static void exibirErroCategoriaVazia() {
        System.out.println("\nPrimeiro insira uma categoria no banco de dados antes de inserir um produto!\n");
    }

    public static void exibirErroProdutoNaoEncontrado() {
        System.out.println("\nEste produto não está cadastrado na base de dados! Digite um ID válido!\n");
    }

    public static void exibirMensagemErroDesconto() {
        System.out.println("\nO valor do desconto precisa estar entre 0 e 99.99%.");
    }

    public static void exibirMensagemErroAumento() {
        System.out.println("\nO valor do aumento precisa ser positivo.");
    }

    public static void exibirErroOpcaoInvalida() {
        System.out.println("\nInsira um valor válido. Tente novamente!");
    }
}