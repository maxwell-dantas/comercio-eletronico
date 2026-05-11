package comercioEletronico.template.admin;

import comercioEletronico.model.entities.Venda;
import comercioEletronico.model.entities.VendaItem;
import comercioEletronico.util.Util;
import comercioEletronico.view.admin.AdminView;

import java.util.ArrayList;
import java.util.Scanner;

public class AdminTemplate {
    private static Scanner scanner = new Scanner(System.in);

    public static void menu() {

        int opcaoMenuAdmin = -1;
        while (opcaoMenuAdmin != 0) {
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
            opcaoMenuAdmin = Util.lerInteiroSeguro(scanner.nextLine());

            try {
                switch (opcaoMenuAdmin) {
                    case 0:
                        System.out.println("\nVoltando ao Sistema de Login...");
                        break;

                    case 1:
                        AdminClienteTemplate.menu();
                        break;

                    case 2:
                        AdminCategoriaTemplate.menu();
                        break;

                    case 3:
                        AdminProdutoTemplate.menu();
                        break;

                    case 4:
                        AdminProdutoTemplate.menuReajustePreco();
                        break;

                    case 5:
                        listarVendas();
                        break;

                    default:
                        System.out.println("\nInsira um valor válido. Tente novamente!\n");
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public static void listarVendas() {
        try {
            ArrayList<Venda> listaVendas = AdminView.obterVendas();
            ArrayList<VendaItem> listaItems = AdminView.obterVendaItems();

            System.out.println("\n=== VENDAS CADASTRADAS ===\n");

            int contadorVenda = 1;
            for (Venda venda : listaVendas) {
                if (!venda.getCarrinho()) {
                    System.out.println(contadorVenda + " - ID venda: " + venda.getId() + " - " + venda + " - ID cliente: " + venda.getIdCliente());

                    int contadorItem = 1;
                    for (VendaItem vendaItem : listaItems) {
                        if (vendaItem.getIdVenda() == venda.getId()) {
                            System.out.println("    " + contadorItem + " - " + vendaItem);
                            contadorItem++;
                        }
                    }
                    contadorVenda++;
                    System.out.println();
                }
            }
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}