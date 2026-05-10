package comercioEletronico.template.admin;

import comercioEletronico.model.entities.Venda;
import comercioEletronico.model.entities.VendaItem;
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

    public static void listarVendas(ArrayList<Venda> listaVendas, ArrayList<VendaItem> vendaItems) {
        System.out.println("\n=== VENDAS CADASTRADAS ===\n");
        for (Venda venda : listaVendas) {
            System.out.println("ID venda: " + venda.getId() + " - " + venda + " - ID cliente: " + venda.getIdCliente());

            int contadorItem = 1;
            for (VendaItem vendaItem : vendaItems) {
                System.out.println("    "+ contadorItem + " - " + vendaItem);
                contadorItem++;
            }
            System.out.println();
        }
    }

    // MÉTODOS DE FEEDBACK

    public static void exibirMensagemListaVazia() {
        System.out.println("\nAinda não há nenhuma venda cadastrada na base de dados!\n");
    }

    public static void exibirErroOpcaoInvalida() {
        System.out.println("\nInsira um valor válido. Tente novamente!\n");
    }

    public static void exibirMensagemSaida() {
        System.out.println("\nVoltando ao Sistema de Login...");
    }
}