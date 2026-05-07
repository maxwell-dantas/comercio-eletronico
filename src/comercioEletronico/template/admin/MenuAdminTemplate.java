package comercioEletronico.template.admin;

import comercioEletronico.view.Util;

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
                
                Digite uma opção: 
                """);
        return Util.lerInteiroSeguro(scanner.nextLine());
    }
}