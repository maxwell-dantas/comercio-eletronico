package comercioEletronico.template.admin;

import comercioEletronico.model.entities.Cliente;
import comercioEletronico.util.Util;
import comercioEletronico.view.admin.AdminClienteView;

import java.util.ArrayList;
import java.util.Scanner;

public class AdminClienteTemplate {
    private static Scanner scanner = new Scanner(System.in);

    public static void menu() {
        int id;
        Cliente cliente;
        String nome;
        String telefone;
        String email;
        String senha;

        int opcaoMenuAdminCliente = -1;
        while (opcaoMenuAdminCliente != 0) {
            System.out.println("""
                    
                    === CADASTRO DE CLIENTES ===
                    
                    1. Listar Clientes
                    2. Inserir Cliente
                    3. Atualizar Cliente
                    4. Remover Cliente
                    0. Sair
                    """);
            System.out.print("Digite uma opção: ");
            opcaoMenuAdminCliente = Util.lerInteiroSeguro(scanner.nextLine());

            try {
                switch (opcaoMenuAdminCliente) {
                    case 0:
                        System.out.println("\nVoltando ao Menu Principal...");
                        break;

                    case 1:
                        listarClientes();
                        break;

                    case 2:
                        System.out.println("\n=== CADASTRAR CLIENTE ===\n");

                        System.out.print("Digite o nome do cliente: ");
                        nome = scanner.nextLine();

                        System.out.print("Digite o telefone do cliente: ");
                        telefone = scanner.nextLine();

                        System.out.print("Digite o e-mail do cliente: ");
                        email = scanner.nextLine();

                        System.out.print("Digite a senha do cliente: ");
                        senha = scanner.nextLine();

                        AdminClienteView.inserir(nome, telefone, email, senha);
                        System.out.println("\nCliente cadastrado com sucesso!\n");
                        break;

                    case 3:
                        if (AdminClienteView.obterClientes().isEmpty()) {
                            System.out.println("\nAinda não há nenhum cliente cadastrado no sistema!");
                            continue;
                        }

                        System.out.println("\n=== ATUALIZAR CLIENTE ===\n");

                        System.out.print("Digite o ID do cliente: ");
                        id = Util.lerInteiroSeguro(scanner.nextLine());

                        if (id == 1) {
                            System.out.println("\nOs valores do administrador não podem ser alterados!");
                            continue;
                        }

                        cliente = AdminClienteView.listarId(id);

                        if (cliente == null) {
                            System.out.println("\nEste cliente não está cadastrado no sistema! Digite um ID válido!");
                            continue;
                        }

                        System.out.print("Digite o novo nome do cliente: ");
                        nome = scanner.nextLine();

                        System.out.print("Digite o novo número de telefone do cliente: ");
                        telefone = scanner.nextLine();

                        System.out.print("Digite o novo e-mail do cliente: ");
                        email = scanner.nextLine();

                        System.out.print("Digite a nova senha do cliente: ");
                        senha = scanner.nextLine();

                        AdminClienteView.atualizar(cliente, nome, telefone, email, senha);
                        System.out.println("\nCliente atualizado com sucesso!\n");
                        break;

                    case 4:
                        if (AdminClienteView.obterClientes().isEmpty()) {
                            System.out.println("\nAinda não há nenhum cliente cadastrado no sistema!");
                            continue;
                        }

                        System.out.println("\n=== REMOVER CLIENTE ===\n");

                        System.out.print("Digite o ID do cliente: ");
                        id = Util.lerInteiroSeguro(scanner.nextLine());

                        if (id == 1) {
                            System.out.println("\nNão é possível remover o cadastro do administrador!");
                            continue;
                        }

                        cliente = AdminClienteView.listarId(id);

                        if (cliente == null) {
                            System.out.println("\nEste cliente não está cadastrado no sistema! Digite um ID válido!");
                            continue;
                        }

                        AdminClienteView.remover(cliente);
                        System.out.println("\nCliente removido com sucesso!\n");
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

    public static void listarClientes() {
        try {
            ArrayList<Cliente> listaClientes = AdminClienteView.obterClientes();
            System.out.println("\n=== CLIENTES CADASTRADOS ===\n");

            for (Cliente cliente : listaClientes) {
                System.out.println(cliente);
            }

            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}