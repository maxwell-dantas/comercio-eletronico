package comercioEletronico.view.admin;

import comercioEletronico.model.entities.Cliente;
import comercioEletronico.template.admin.AdminClienteTemplate;

import comercioEletronico.model.dao.ClienteDao;
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
                    Util.exibirMensagem("\nVoltando ao Menu Principal...\n");
                    break;

                case 1:
                    if (clienteDao.listar().isEmpty()) {
                        Util.exibirMensagem("\nAinda não há nenhum cliente cadastrado na base de dados!");
                        continue;
                    }

                    AdminClienteTemplate.listarClientes(clienteDao.listar());
                    Util.pausar();
                    break;

                case 2:
                    Util.exibirMensagem("\n=== CADASTRO DE CLIENTE ===\n");
                    dados = AdminClienteTemplate.obterDados();
                    boolean emailDisponivel = clienteDao.isEmailDisponivel(dados[1]);

                    if (!emailDisponivel) {
                        Util.exibirMensagem("\nEste e-mail já está em uso. Por favor, faça login ou use outro e-mail");
                        continue;
                    }

                    try {
                        cliente = new Cliente(dados[0], dados[1], dados[2], dados[3]);
                        clienteDao.inserir(cliente);
                        Util.exibirMensagem("\nCliente cadastrado com sucesso!\n");
                        Util.pausar();
                    } catch (IllegalArgumentException e) {
                        Util.exibirMensagem("\n" + e.getMessage());
                    }
                    break;

                case 3:
                    Util.exibirMensagem("\n=== ATUALIZAÇÃO DE CLIENTE ===\n");

                    if (clienteDao.listar().isEmpty()) {
                        Util.exibirMensagem("Ainda não há nenhum cliente cadastrado na base de dados!");
                        continue;
                    }

                    id = AdminClienteTemplate.obterId();
                    cliente = clienteDao.listarId(id);

                    if (cliente == null) {
                        Util.exibirMensagem("Este cliente não está cadastrado na base de dados! Digite um ID válido!");
                        continue;
                    }

                    dados = AdminClienteTemplate.obterDados();
                    try {
                        clienteDao.atualizar(id, dados[0], dados[1], dados[2], dados[3]);
                        Util.exibirMensagem("\nCliente atualizado com sucesso!\n");
                        Util.pausar();
                    } catch (IllegalArgumentException e) {
                        Util.exibirMensagem("\n" + e.getMessage());
                    }
                    break;

                case 4:
                    Util.exibirMensagem("\n=== REMOÇÃO DE CLIENTE ===\n");

                    if (clienteDao.listar().isEmpty()) {
                        Util.exibirMensagem("Ainda não há nenhum cliente cadastrado na base de dados!");
                        continue;
                    }

                    id = AdminClienteTemplate.obterId();
                    cliente = clienteDao.listarId(id);

                    if (cliente == null) {
                        Util.exibirMensagem("Este cliente não está cadastrado na base de dados! Digite um ID válido!");
                        continue;
                    }

                    clienteDao.remover(id);
                    Util.exibirMensagem("\nCliente removido com sucesso!\n");
                    Util.pausar();
                    break;

                default:
                    Util.exibirMensagem("\nInsira um valor válido. Tente novamente!");
                    break;
            }
        } while (opcaoCrud != 0);
    }
}