package comercioEletronico.template.admin;

import comercioEletronico.view.Util;

import java.util.Scanner;

public class AdminCategoriaTemplate {
    private static Scanner scanner = new Scanner(System.in);

    public static int obterOpcaoCrud() {
        System.out.println("""
                === CADASTRO DE CATEGORIAS ===
                
                1. Listar Categorias
                2. Inserir Categoria
                3. Atualizar Categoria
                4. Remover Categoria
                0. Sair
                
                Digite uma opção: 
                """);
        return Util.lerInteiroSeguro(scanner.nextLine());
    }

    public static int obterId() {
        System.out.print("Digite o ID da categoria: ");
        return Util.lerInteiroSeguro(scanner.nextLine());
    }

    public static String obterDados() {
        System.out.print("Digite a descrição da categoria: ");
        return scanner.nextLine();
    }
}