package comercioEletronico.view;

public class Util {

    // valida se a entrada é um número
    public static int lerInteiroSeguro(String entrada) {
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            return -1; // retorna -1 (valor para switch-case default)
        }
    }

    public static double lerDoubleSeguro(String entrada) {
        try {
            return Double.parseDouble(entrada);
        } catch (NumberFormatException e) {
            return -1; // retorna -1 (valor para switch-case default)
        }
    }

    public static void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public static void pausar() {
        System.out.print("Pressione ENTER para continuar...");
        try {
            System.in.read();
            while (System.in.available() > 0) {
                System.in.read();
            }
        } catch (Exception e) {
        }
    }
}