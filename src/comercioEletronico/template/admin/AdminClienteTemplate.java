package comercioEletronico.template.admin;

import comercioEletronico.model.entities.Cliente;
import comercioEletronico.view.Util;

import java.util.ArrayList;
import java.util.Scanner;

public class AdminClienteTemplate {
    private static Scanner scanner = new Scanner(System.in);

    public static int obterOpcaoCrud() {
        System.out.println("""
                
                === CADASTRO DE CLIENTES ===
                
                1. Listar Clientes
                2. Inserir Cliente
                3. Atualizar Cliente
                4. Remover Cliente
                0. Sair
                """);
        System.out.print("Digite uma opção: ");
        return Util.lerInteiroSeguro(scanner.nextLine());
    }

    public static int obterId() {
        System.out.print("Digite o ID do cliente: ");
        return Util.lerInteiroSeguro(scanner.nextLine());
    }

    public static String[] obterDados() {
        String[] dados = new String[4];

        System.out.print("Digite o nome do cliente: ");
        dados[0] = scanner.nextLine();

        System.out.print("Digite o e-mail do cliente: ");
        dados[1] = scanner.nextLine();

        System.out.print("Digite o telefone do cliente: ");
        dados[2] = scanner.nextLine();

        System.out.print("Digite a senha do cliente: ");
        dados[3] = scanner.nextLine();

        return dados;
    }

    public static void listarClientes(ArrayList<Cliente> listaClientes) {
        System.out.println("\n=== CLIENTES CADASTRADOS ===\n");
        for (Cliente cliente : listaClientes) {
            System.out.println(cliente);
        }
        System.out.println();
    }

    // MÉTODOS DE FEEDBACK

    public static void exibirMensagemSaida() {
        System.out.println("\nVoltando ao Menu Principal...");
    }

    public static void exibirMensagemListaVazia() {
        System.out.println("\nAinda não há nenhum cliente cadastrado na base de dados!");
    }

    public static void exibirCabecalho(String operacao) {
        System.out.println("\n=== " + operacao.toUpperCase() + " CLIENTE ===\n");
    }

    public static void exibirSucesso(String acao) {
        System.out.println("\nCliente " + acao + " com sucesso!\n");
    }

    public static void exibirErro(String erro) {
        System.out.println("\n" + erro);
    }

    public static void exibirErroEmailEmUso() {
        System.out.println("\nEste e-mail já está em uso. Por favor, use outro e-mail.");
    }

    public static void exibirErroClienteNaoEncontrado() {
        System.out.println("\nEste cliente não está cadastrado na base de dados! Digite um ID válido!");
    }

    public static void exibirErroAtualizarAdmin() {
        System.out.println("\nOs valores do administrador não podem ser alterados!");
    }

    public static void exibirErroRemoverAdmin() {
        System.out.println("\nNão é possível remover o cadastro do administrador!");
    }

    public static void exibirErroOpcaoInvalida() {
        System.out.println("\nInsira um valor válido. Tente novamente!");
    }
}