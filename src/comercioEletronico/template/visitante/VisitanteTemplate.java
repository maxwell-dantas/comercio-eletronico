package comercioEletronico.template.visitante;

import comercioEletronico.template.admin.AdminTemplate;
import comercioEletronico.template.cliente.ClienteTemplate;
import comercioEletronico.util.Util;
import comercioEletronico.view.visitante.VisitanteView;

import java.util.Scanner;

public class VisitanteTemplate {
    private static Scanner scanner = new Scanner(System.in);

    public static void menuLogin() {
        String nome;
        String telefone;
        String email;
        String senha;

        int opcaoMenuLogin = -1;
        while (opcaoMenuLogin != 0) {
            System.out.println("""
                    
                    === SISTEMA DE LOGIN ===
                    
                    1. Entrar
                    2. Criar conta
                    0. Sair
                    """);
            System.out.print("Digite uma opção: ");
            opcaoMenuLogin = Util.lerInteiroSeguro(scanner.nextLine());

            try {
                switch (opcaoMenuLogin) {
                    case 0:
                        System.out.println("\nSistema encerrado! Até logo...");
                        break;

                    case 1:
                        System.out.print("\nDigite o seu e-mail: ");
                        email = scanner.nextLine();

                        System.out.print("Digite a sua senha: ");
                        senha = scanner.nextLine();

                        int idUsuario = VisitanteView.entrar(email, senha);

                        if (idUsuario == 0) {
                            System.out.println("\nE-mail ou senha inválidos!\n");
                            continue;
                        }

                        System.out.println("\nBem vindo(a), " + VisitanteView.obterUsuario(idUsuario).getNome() +"!");

                        if (idUsuario == 1) { // ID do admin sempre é 1, pois nasce com a aplicação
                            AdminTemplate.menu();
                            continue;
                        }

                        ClienteTemplate.menu(idUsuario);
                        break;

                    case 2:
                        System.out.print("\nDigite o seu nome: ");
                        nome = scanner.nextLine();

                        System.out.print("Digite o seu número de telefone: ");
                        telefone = scanner.nextLine();

                        System.out.print("Digite o seu e-mail: ");
                        email = scanner.nextLine();

                        System.out.print("Digite a sua senha: ");
                        senha = scanner.nextLine();

                        VisitanteView.criarConta(nome, telefone, email, senha);
                        System.out.println("\nConta cadastrada com sucesso!\n");
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
}