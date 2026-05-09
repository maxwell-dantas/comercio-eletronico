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
        System.out.println("\n=== VENDAS CADASTRADAS ===\n");

        if (listaVendas.isEmpty()) {
            System.out.println("Nenhuma venda encontrada no momento.\n");
        } else {
            for (Venda venda : listaVendas) {
                System.out.println(venda);
            }
        }
    }

    // MÉTODOS DE FEEDBACK

    public static void exibirErroOpcaoInvalida() {
        System.out.println("\nInsira um valor válido. Tente novamente!");
    }

    public static void exibirMensagemSaida() {
        System.out.println("\nVoltando ao Sistema de Login...\n");
    }
}