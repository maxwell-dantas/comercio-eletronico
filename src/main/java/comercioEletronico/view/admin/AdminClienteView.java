package comercioEletronico.view.admin;

import comercioEletronico.model.dao.VendaDao;
import comercioEletronico.model.entities.Cliente;
import comercioEletronico.model.dao.ClienteDao;
import comercioEletronico.model.entities.Venda;
import comercioEletronico.view.cliente.ClienteView;

import java.util.ArrayList;

public class AdminClienteView {
    private static ClienteDao clienteDao = new ClienteDao();
    private static VendaDao vendaDao = new VendaDao();

    public static ArrayList<Cliente> obterClientes() {
        if (clienteDao.listar().isEmpty()) {
            throw new IllegalArgumentException("\nAinda não há nenhum cliente cadastrado no sistema!");
        }
        return clienteDao.listar();
    }

    public static void inserir(String nome, String telefone, String email, String senha, int idFuncao) {
        if (!clienteDao.isEmailDisponivel(email)) {
            throw new IllegalArgumentException("\nEste e-mail já está cadastrado no sistema.");
        }

        Cliente cliente = new Cliente(nome, telefone, email, senha, idFuncao);
        clienteDao.inserir(cliente);
    }

    public static Cliente listarId(int id) {
        return clienteDao.listarId(id);
    }

    public static void atualizar(Cliente cliente, String nome, String telefone, String email, String senha, int idFuncao) {
        if (cliente.getId() == 1 && idFuncao != 1) {
            throw new IllegalArgumentException("\nNão é possível alterar a função do ADMIN principal!");
        }

        if (!cliente.getEmail().equalsIgnoreCase(email) && !clienteDao.isEmailDisponivel(email)) {
            throw new IllegalArgumentException("\nEste e-mail já está cadastrado no sistema.");
        }

        // se a função do cliente (2) for alterada, apaga o carrinho em aberto
        if (cliente.getIdFuncao() == 2 && idFuncao != 2) {
            Venda venda = ClienteView.buscarCarrinhoAberto(cliente.getId());

            if (venda != null) {
                ClienteView.limparCarrinho(venda.getId());
                vendaDao.remover(venda.getId());
            }
        }

        cliente.setNome(nome);
        cliente.setTelefone(telefone);
        cliente.setEmail(email);
        cliente.setSenha(senha);
        cliente.setIdFuncao(idFuncao);
        clienteDao.atualizar(cliente);
    }

    public static void remover(Cliente cliente) {
        clienteDao.remover(cliente.getId());
    }
}