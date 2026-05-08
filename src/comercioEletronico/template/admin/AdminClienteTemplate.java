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
                
                Digite uma opção: 
                """);
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
        System.out.println("=== CLIENTES CADASTRADOS ===\n");

        if (listaClientes.isEmpty()) {
            System.out.println("Nenhum cliente encontrado no sistema.");
        } else {
            for (Cliente cliente : listaClientes) {
                System.out.println(cliente);
            }
        }
    }
}