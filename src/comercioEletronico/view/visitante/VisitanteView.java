package comercioEletronico.view.visitante;

import comercioEletronico.model.dao.ClienteDao;
import comercioEletronico.model.entities.Cliente;
import comercioEletronico.template.visitante.VisitanteTemplate;
import comercioEletronico.view.Util;
import comercioEletronico.view.admin.MenuAdminView;
import comercioEletronico.view.cliente.ClienteView;

public class VisitanteView {
    private ClienteDao clienteDao = new ClienteDao();
    private MenuAdminView menuAdminView = new MenuAdminView();
    private ClienteView clienteView = new ClienteView();

    public void exibirMenu() {
        Cliente cliente;
        String[] dados;
        int idVisitante;
        int opcaoMenuLogin;

        if (clienteDao.listar().isEmpty()) {
            cliente = new Cliente("admin", "admin", "admin", "admin");
            clienteDao.inserir(cliente);
        }

        do {
            opcaoMenuLogin = VisitanteTemplate.obterOpcaoMenuLogin();

            switch (opcaoMenuLogin) {
                case 0:
                    VisitanteTemplate.exibirMensagemSaida();
                    break;

                case 1:
                    // [0] email, [1] senha
                    dados = VisitanteTemplate.obterDadosLogin();
                    idVisitante = clienteDao.obterIdVisitanteLogin(dados[0], dados[1]);

                    if (idVisitante == 0) {
                        VisitanteTemplate.exibirErroLogin();
                        Util.pausar();
                        continue;
                    }

                    // 1 - admin, 2 - cliente
                    int adminCliente = clienteDao.obterAdminCliente(idVisitante);

                    switch (adminCliente) {
                        case 1:
                            menuAdminView.exibirMenu();
                            continue;

                        case 2:
                            // Passando o ID do cliente para a View do Cliente
                            clienteView.exibirMenu(idVisitante);
                            continue;
                    }
                    break;

                case 2:
                    // [0] nome, [1] email, [2] telefone, [3] senha
                    dados = VisitanteTemplate.obterDados();

                    if (!clienteDao.isEmailDisponivel(dados[1])) {
                        VisitanteTemplate.exibirErroEmailEmUso();
                        Util.pausar();
                        continue;
                    }

                    try {
                        cliente = new Cliente(dados[0], dados[1], dados[2], dados[3]);
                        clienteDao.inserir(cliente); // salva o cliente no banco de dados (arquivo json)
                        VisitanteTemplate.exibirSucessoCadastro();
                        Util.pausar();
                    } catch (IllegalArgumentException e) {
                        VisitanteTemplate.exibirErro(e.getMessage());
                        Util.pausar();
                    }
                    break;

                default:
                    VisitanteTemplate.exibirErroOpcaoInvalida();
                    break;
            }

        } while (opcaoMenuLogin != 0);
    }
}