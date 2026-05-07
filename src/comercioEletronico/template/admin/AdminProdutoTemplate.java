package comercioEletronico.template.admin;

import java.util.Scanner;

import comercioEletronico.view.Util;

public class AdminProdutoTemplate {
    private static Scanner scanner = new Scanner(System.in);

    public static int obterOpcaoCrud() {
        System.out.println("""
                === CADASTRO DE PRODUTOS ===
                
                1. Listar Produtos
                2. Inserir Produto
                3. Atualizar Produto
                4. Remover Produto
                0. Sair
                
                Digite uma opção: 
                """);
        return Util.lerInteiroSeguro(scanner.nextLine());
    }

    public static int obterId() {
        System.out.print("Digite o ID do produto: ");
        return Util.lerInteiroSeguro(scanner.nextLine());
    }

    public static String[] obterDados() {
        String[] dados = new String[4];

        System.out.print("Digite a descrição produto: ");
        dados[0] = scanner.nextLine();

        System.out.print("Digite o preço do produto: ");
        dados[1] = scanner.nextLine();

        System.out.print("Digite a quantidade em estoque do produto: ");
        dados[2] = scanner.nextLine();

        System.out.print("Digite o ID da categoria para este produto: ");
        dados[3] = scanner.nextLine();
        return dados;
    }
}