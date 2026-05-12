package comercioEletronico.template.admin;

import comercioEletronico.model.entities.Categoria;
import comercioEletronico.util.Util;
import comercioEletronico.view.admin.AdminCategoriaView;

import java.util.ArrayList;
import java.util.Scanner;

public class AdminCategoriaTemplate {
    private static Scanner scanner = new Scanner(System.in);

    public static void menu() {
        int id;
        Categoria categoria;
        String descricao;

        int opcaoMenuAdminCategoria = -1;
        while (opcaoMenuAdminCategoria != 0) {
            System.out.println("""
                    
                    === CADASTRO DE CATEGORIAS ===
                    
                    1. Listar Categorias
                    2. Inserir Categoria
                    3. Atualizar Categoria
                    4. Remover Categoria
                    0. Sair
                    """);
            System.out.print("Digite uma opção: ");
            opcaoMenuAdminCategoria = Util.lerInteiroSeguro(scanner.nextLine());

            try {
                switch (opcaoMenuAdminCategoria) {
                    case 0:
                        System.out.println("\nVoltando ao Menu Principal...");
                        break;

                    case 1:
                        listarCategorias();
                        break;

                    case 2:
                        System.out.println("\n=== CADASTRAR CATEGORIA ===\n");

                        System.out.print("Digite a descrição da categoria: ");
                        descricao = scanner.nextLine();

                        AdminCategoriaView.inserir(descricao);
                        System.out.println("\nCategoria cadastrada com sucesso!\n");
                        break;

                    case 3:
                        if (AdminCategoriaView.obterCategorias().isEmpty()) {
                            System.out.println("\nAinda não há nenhuma categoria cadastrada no sistema!");
                            continue;
                        }

                        System.out.println("\n=== ATUALIZAR CATEGORIA ===\n");

                        System.out.print("Digite o ID da categoria: ");
                        id = Util.lerInteiroSeguro(scanner.nextLine());

                        categoria = AdminCategoriaView.listarId(id);

                        if (categoria == null) {
                            System.out.println("\nEsta categoria não está cadastrada no sistema! Digite um ID válido!");
                            continue;
                        }

                        System.out.print("Digite a nova descrição: ");
                        descricao = scanner.nextLine();

                        AdminCategoriaView.atualizar(categoria, descricao);
                        System.out.println("\nCategoria atualizada com sucesso!\n");
                        break;

                    case 4:
                        if (AdminCategoriaView.obterCategorias().isEmpty()) {
                            System.out.println("\nAinda não há nenhuma categoria cadastrada no sistema!");
                            continue;
                        }

                        System.out.println("\n=== REMOVER CATEGORIA ===\n");

                        System.out.print("Digite o ID da categoria: ");
                        id = Util.lerInteiroSeguro(scanner.nextLine());

                        categoria = AdminCategoriaView.listarId(id);

                        if (categoria == null) {
                            System.out.println("\nEsta categoria não está cadastrada no sistema! Digite um ID válido!");
                            continue;
                        }

                        AdminCategoriaView.remover(categoria);
                        System.out.println("\nCategoria removida com sucesso!\n");
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

    public static void listarCategorias() {
        try {
            ArrayList<Categoria> listaCategorias = AdminCategoriaView.obterCategorias();
            System.out.println("\n=== CATEGORIAS CADASTRADAS ===\n");

            for (Categoria categoria : listaCategorias) {
                System.out.println(categoria);
            }

            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}