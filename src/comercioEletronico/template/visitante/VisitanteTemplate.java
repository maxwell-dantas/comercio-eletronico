package comercioEletronico.template.visitante;

import comercioEletronico.view.Util;

import java.util.Scanner;

public class VisitanteTemplate {
    private static Scanner scanner = new Scanner(System.in);

    public static int obterOpcaoMenuLogin() {
        System.out.println("""
                
                === SISTEMA DE LOGIN ===
                
                1. Entrar
                2. Criar conta
                0. Sair
                """);
        System.out.print("Digite uma opção: ");
        return Util.lerInteiroSeguro(scanner.nextLine());
    }

    public static String[] obterDados() {
        String[] dados = new String[4];

        System.out.print("\nDigite o seu nome: ");
        dados[0] = scanner.nextLine();

        System.out.print("Digite o seu e-mail: ");
        dados[1] = scanner.nextLine();

        System.out.print("Digite o seu número de telefone: ");
        dados[2] = scanner.nextLine();

        System.out.print("Digite a sua senha: ");
        dados[3] = scanner.nextLine();

        return dados;
    }

    public static String[] obterDadosLogin() {
        String[] dados = new String[2];

        System.out.print("\nDigite o seu e-mail: ");
        dados[0] = scanner.nextLine();

        System.out.print("Digite a sua senha: ");
        dados[1] = scanner.nextLine();

        return dados;
    }

    // MÉTODOS DE FEEDBACK

    public static void exibirSucessoCadastro() {
        System.out.println("\nConta cadastrada com sucesso!\n");
    }

    public static void exibirErroLogin() {
        System.out.println("\nE-mail ou senha inválidos!\n");
    }

    public static void exibirErro(String erro) {
        System.out.println("\n" + erro);
    }

    public static void exibirErroEmailEmUso() {
        System.out.println("\nEste e-mail já está em uso. Por favor, faça login ou use outro e-mail.\n");
    }

    public static void exibirErroOpcaoInvalida() {
        System.out.println("\nInsira um valor válido. Tente novamente!");
    }

    public static void exibirMensagemSaida() {
        System.out.println("\nSistema encerrado! Até logo...");
    }
}