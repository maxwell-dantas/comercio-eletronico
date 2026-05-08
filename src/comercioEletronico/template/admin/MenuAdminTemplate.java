package comercioEletronico.template.admin;

import comercioEletronico.model.entities.Venda;
import comercioEletronico.view.Util;

import java.util.ArrayList;
import java.util.Scanner;

public class MenuAdminTemplate {
    private static Scanner scanner = new Scanner(System.in);

    public static int obterOpcaoPrincipal() {
        System.out.println("""
                === MENU ADMINISTRADOR ===
                
                1. Cadastro de Clientes
                2. Cadastro de Categorias
                3. Cadastro de Produtos
                4. Reajustar Preços de Produtos
                5. Listar Vendas
                0. Sair
                """);
        System.out.print("Digite uma opção: ");
        return Util.lerInteiroSeguro(scanner.nextLine());
    }

    public static void listarVendas(ArrayList<Venda> listaVendas) {
        System.out.println("=== VENDAS CADASTRADAS ===\n");

        if (listaVendas.isEmpty()) {
            System.out.println("Nenhuma venda encontrada no momento.");
        } else {
            for (Venda venda : listaVendas) {
                System.out.println(venda);
            }
        }
    }
}