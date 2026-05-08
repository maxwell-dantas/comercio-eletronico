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
                    Util.exibirmensagem("Voltando ao Menu Principal...");
                    Util.pausar();
                    break;

                case 1:
                    AdminClienteTemplate.listarClientes(clienteDao.listar());
                    Util.pausar();
                    break;

                case 2:
                    Util.exibirmensagem("=== CADASTRO DE CLIENTE ===");
                    dados = AdminClienteTemplate.obterDados();
                    boolean emailDisponivel = clienteDao.isEmailDisponivel(dados[1]);

                    if (!emailDisponivel) {
                        Util.exibirmensagem("Este e-mail já está em uso. Por favor, faça login ou use outro e-mail");
                        Util.pausar();
                        continue;
                    }

                    try {
                        cliente = new Cliente(dados[0], dados[1], dados[2], dados[3]);
                        clienteDao.inserir(cliente);
                        Util.exibirmensagem("Cliente cadastrado com sucesso!");
                        Util.pausar();
                    } catch (IllegalArgumentException e) {
                        Util.exibirmensagem("Erro de validação: " + e.getMessage());
                        Util.pausar();
                    }
                    break;

                case 3:
                    Util.exibirmensagem("=== ATUALIZAÇÃO DE CLIENTE ===");
                    id = AdminClienteTemplate.obterId();
                    cliente = clienteDao.listarId(id);

                    if (cliente == null) {
                        Util.exibirmensagem("Este cliente não está cadastrado na base de dados! Digite um ID válido!");
                        Util.pausar();
                        continue;
                    }

                    dados = AdminClienteTemplate.obterDados();
                    try {
                        clienteDao.atualizar(id, dados[0], dados[1], dados[2], dados[3]);
                        Util.exibirmensagem("Cliente atualizado com sucesso!");
                        Util.pausar();
                    } catch (IllegalArgumentException e) {
                        Util.exibirmensagem("Erro de validação: " + e.getMessage());
                        Util.pausar();
                    }
                    break;

                case 4:
                    Util.exibirmensagem("=== REMOVER CLIENTE ===");
                    id = AdminClienteTemplate.obterId();
                    cliente = clienteDao.listarId(id);

                    if (cliente == null) {
                        Util.exibirmensagem("Este cliente não está cadastrado na base de dados! Digite um ID válido!");
                        Util.pausar();
                        continue;
                    }

                    clienteDao.remover(id);
                    Util.exibirmensagem("Cliente removido com sucesso!");
                    Util.pausar();
                    break;

                default:
                    Util.exibirmensagem("Insira um valor válido. Tente novamente!");
                    Util.pausar();
                    break;
            }

        } while (opcaoCrud != 0);
    }
}
