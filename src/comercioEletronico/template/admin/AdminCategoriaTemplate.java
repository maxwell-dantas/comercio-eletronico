package comercioEletronico.template.admin;

import comercioEletronico.model.entities.Categoria;
import comercioEletronico.view.Util;

import java.util.ArrayList;
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
                """);
        System.out.print("Digite uma opção: ");
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

    public static void listarCategorias(ArrayList<Categoria> listaCategorias) {
        System.out.println("\n=== CATEGORIAS CADASTRADAS ===\n");
        for (Categoria categoria : listaCategorias) {
            System.out.println(categoria);
        }
        System.out.println();
    }

    // MÉTODOS DE FEEDBACK

    public static void exibirMensagemSaida() {
        System.out.println("\nVoltando ao Menu Principal...\n");
    }

    public static void exibirMensagemListaVazia() {
        System.out.println("\nAinda não há nenhuma categoria cadastrada na base de dados!");
    }

    public static void exibirCabecalho(String operacao) {
        System.out.println("\n=== " + operacao.toUpperCase() + " CATEGORIA ===\n");
    }

    public static void exibirSucesso(String acao) {
        System.out.println("\nCategoria " + acao + " com sucesso!\n");
    }

    public static void exibirErro(String erro) {
        System.out.println("\n" + erro);
    }

    public static void exibirErroCategoriaNaoEncontrada() {
        System.out.println("\nEsta categoria não está cadastrada na base de dados! Digite um ID válido!");
    }

    public static void exibirErroOpcaoInvalida() {
        System.out.println("\nInsira um valor válido. Tente novamente!");
    }
}