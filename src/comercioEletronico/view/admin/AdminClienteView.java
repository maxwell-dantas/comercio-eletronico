package comercioEletronico.view.admin;

import comercioEletronico.model.entities.Cliente;
import comercioEletronico.model.dao.ClienteDao;

import java.util.ArrayList;

public class AdminClienteView {
    private static ClienteDao clienteDao = new ClienteDao();

    public static ArrayList<Cliente> obterClientes() {
        if (clienteDao.listar().isEmpty()) {
            throw new IllegalArgumentException("\nAinda não há nenhum cliente cadastrado no sistema!\n");
        }
        return clienteDao.listar();
    }

    public static void inserir(String nome, String telefone, String email, String senha) {
        if (!clienteDao.isEmailDisponivel(email)) {
            throw new IllegalArgumentException("Este e-mail já está cadastrado no sistema.");
        }

        Cliente cliente = new Cliente(nome, telefone, email, senha);
        clienteDao.inserir(cliente);
    }

    public static Cliente listarId(int id) {
        return clienteDao.listarId(id);
    }

    public static void atualizar(Cliente cliente, String nome, String telefone, String email, String senha) {
        if (!cliente.getEmail().equalsIgnoreCase(email) && !clienteDao.isEmailDisponivel(email)) {
            throw new IllegalArgumentException("Este e-mail já está cadastrado no sistema.");
        }
        clienteDao.atualizar(cliente.getId(), nome, telefone, email, senha);
    }

    public static void remover(Cliente cliente) {
        clienteDao.remover(cliente.getId());
    }
}