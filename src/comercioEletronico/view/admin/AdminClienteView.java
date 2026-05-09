package comercioEletronico.view.admin;

import comercioEletronico.model.entities.Cliente;
import comercioEletronico.model.dao.ClienteDao;
import comercioEletronico.template.admin.AdminClienteTemplate;
import comercioEletronico.view.Util;

public class AdminClienteView {
    ClienteDao clienteDao = new ClienteDao();

    public void exibirMenu() {
        Cliente cliente;
        int opcaoCrud;
        int id;
        String[] dados;

        do {
            opcaoCrud = AdminClienteTemplate.obterOpcaoCrud();

            switch (opcaoCrud) {
                case 0:
                    AdminClienteTemplate.exibirMensagemSaida();
                    break;

                case 1:
                    if (clienteDao.listar().isEmpty()) {
                        AdminClienteTemplate.exibirMensagemListaVazia();
                        continue;
                    }

                    AdminClienteTemplate.listarClientes(clienteDao.listar());
                    Util.pausar();
                    break;

                case 2:
                    AdminClienteTemplate.exibirCabecalho("Cadastrar");
                    dados = AdminClienteTemplate.obterDados();

                    if (!clienteDao.isEmailDisponivel(dados[1])) {
                        AdminClienteTemplate.exibirErroEmailEmUso();
                        continue;
                    }

                    try {
                        cliente = new Cliente(dados[0], dados[1], dados[2], dados[3]);
                        clienteDao.inserir(cliente);
                        AdminClienteTemplate.exibirSucesso("cadastrado");
                        Util.pausar();
                    } catch (IllegalArgumentException e) {
                        AdminClienteTemplate.exibirErro(e.getMessage());
                    }
                    break;

                case 3:
                    if (clienteDao.listar().isEmpty()) {
                        AdminClienteTemplate.exibirMensagemListaVazia();
                        continue;
                    }
                    AdminClienteTemplate.exibirCabecalho("Atualizar");

                    id = AdminClienteTemplate.obterId();
                    cliente = clienteDao.listarId(id);

                    if (cliente == null) {
                        AdminClienteTemplate.exibirErroClienteNaoEncontrado();
                        continue;
                    }

                    dados = AdminClienteTemplate.obterDados();

                    if (!cliente.getEmail().equalsIgnoreCase(dados[1]) && !clienteDao.isEmailDisponivel(dados[1])) {
                        AdminClienteTemplate.exibirErroEmailEmUso();
                        continue;
                    }

                    try {
                        clienteDao.atualizar(id, dados[0], dados[1], dados[2], dados[3]);
                        AdminClienteTemplate.exibirSucesso("atualizado");
                        Util.pausar();
                    } catch (IllegalArgumentException e) {
                        AdminClienteTemplate.exibirErro(e.getMessage());
                    }
                    break;

                case 4:
                    if (clienteDao.listar().isEmpty()) {
                        AdminClienteTemplate.exibirMensagemListaVazia();
                        continue;
                    }
                    AdminClienteTemplate.exibirCabecalho("Remover");

                    id = AdminClienteTemplate.obterId();
                    cliente = clienteDao.listarId(id);

                    if (cliente == null) {
                        AdminClienteTemplate.exibirErroClienteNaoEncontrado();
                        continue;
                    }

                    clienteDao.remover(id);
                    AdminClienteTemplate.exibirSucesso("removido");
                    Util.pausar();
                    break;

                default:
                    AdminClienteTemplate.exibirErroOpcaoInvalida();
                    break;
            }
        } while (opcaoCrud != 0);
    }
}