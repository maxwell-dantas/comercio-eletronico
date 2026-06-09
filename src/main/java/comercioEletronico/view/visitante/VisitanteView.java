package comercioEletronico.view.visitante;

import comercioEletronico.model.dao.ClienteDao;
import comercioEletronico.model.entities.Cliente;

public class VisitanteView {
    private static ClienteDao clienteDao = new ClienteDao();

    public static void inicializarSistema() {
        // inicializa a aplicação com um cadastro admin caso a lista de clientes seja vazia
        if (clienteDao.listar().isEmpty()) {
            Cliente cliente = new Cliente("admin", "admin", "admin", "admin", 1);
            clienteDao.inserir(cliente);
        }
    }

    public static Cliente  entrar(String email, String senha) {
        Cliente clienteLogado = clienteDao.obterUsuarioLogin(email, senha);

        if (clienteLogado == null) {
            throw new IllegalArgumentException("\nE-mail ou senha incorretos!");
        }

        return clienteLogado;
    }

    public static void criarConta(String nome, String telefone, String email, String senha, int idFuncao) {
        if (!clienteDao.isEmailDisponivel(email)) {
            throw new IllegalArgumentException("\nEste e-mail já está em uso. Por favor, faça login ou use outro e-mail.");
        }

        if (idFuncao == 1) {
            throw new IllegalArgumentException("\nO ID da função ao criar a conta não pode ser 1 (admin). Tente 2 (cliente) ou 3 (entregador)!");
        }

        Cliente cliente = new Cliente(nome, telefone, email, senha, idFuncao);
        clienteDao.inserir(cliente);
    }

    public static Cliente obterUsuario(int id) {
        return clienteDao.listarId(id);
    }
}