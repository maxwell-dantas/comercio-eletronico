package comercioEletronico.view.visitante;

import comercioEletronico.model.dao.ClienteDao;
import comercioEletronico.model.entities.Cliente;

public class VisitanteView {
    private static ClienteDao clienteDao = new ClienteDao();

    public static void inicializarSistema() {
        // inicializa a aplicação com um cadastro admin caso a lista de clientes seja vazia
        if (clienteDao.listar().isEmpty()) {
            Cliente cliente = new Cliente("admin", "admin", "admin", "admin");
            clienteDao.inserir(cliente);
        }
    }

    public static int entrar(String email, String senha) {
        int idUsuario = clienteDao.obterIdUsuarioLogin(email, senha);

        if (idUsuario == 0) { // não existe usuário com o ID informado
            return 0;
        }

        return idUsuario;
    }

    public static void criarConta(String nome, String telefone, String email, String senha) {
        Cliente cliente;

        if (!clienteDao.isEmailDisponivel(email)) {
            throw new IllegalArgumentException("\nEste e-mail já está em uso. Por favor, faça login ou use outro e-mail.");
        }

        cliente = new Cliente(nome, telefone, email, senha);
        clienteDao.inserir(cliente);
    }

    public static Cliente obterUsuario(int id) {
        return clienteDao.listarId(id);
    }
}