package comercioEletronico.view.admin;

import comercioEletronico.model.dao.VendaDao;
import comercioEletronico.model.dao.VendaItemDao;
import comercioEletronico.template.admin.MenuAdminTemplate;
import comercioEletronico.view.Util;

public class MenuAdminView {
    private AdminClienteView adminClienteView = new AdminClienteView();
    private AdminCategoriaView adminCategoriaView = new AdminCategoriaView();
    private AdminProdutoView adminProdutoView = new AdminProdutoView();
    private VendaDao vendaDao = new VendaDao();
    private VendaItemDao vendaItemDao = new VendaItemDao();

    public void exibirMenu() {
        int opcaoPrincipal;

        do {
            opcaoPrincipal = MenuAdminTemplate.obterOpcaoPrincipal();

            switch (opcaoPrincipal) {
                case 0:
                    MenuAdminTemplate.exibirMensagemSaida();
                    break;

                case 1:
                    adminClienteView.exibirMenu();
                    break;

                case 2:
                    adminCategoriaView.exibirMenu();
                    break;

                case 3:
                    adminProdutoView.exibirMenu();
                    break;

                case 4:
                    adminProdutoView.reajustarPreco();
                    break;

                case 5:
                    if (vendaDao.listar().isEmpty()) {
                        MenuAdminTemplate.exibirMensagemListaVazia();
                        Util.pausar();
                        continue;
                    }

                    MenuAdminTemplate.listarVendas(vendaDao.listar(), vendaItemDao.listar());
                    Util.pausar();
                    break;

                default:
                    MenuAdminTemplate.exibirErroOpcaoInvalida();
                    break;
            }
        } while (opcaoPrincipal != 0);
    }
}