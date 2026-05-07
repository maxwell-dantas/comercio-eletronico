package comercioEletronico.view;

public class Util {

    // limpa os dados do console para melhor UX
    public static void limparConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // valida se a entrada é um número
    public static int lerInteiroSeguro(String entrada) {
        try {
            return Integer.parseInt(entrada);
        }catch (NumberFormatException e) {
            return -1; // retorna -1 (valor para switch-case default)
        }
    }
}
