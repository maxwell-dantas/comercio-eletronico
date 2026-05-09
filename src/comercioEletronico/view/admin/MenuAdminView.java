package comercioEletronico.view.admin;

import comercioEletronico.model.dao.VendaDao;
import comercioEletronico.template.admin.MenuAdminTemplate;
import comercioEletronico.view.Util;

public class MenuAdminView {
    AdminClienteView adminClienteView = new AdminClienteView();
    AdminCategoriaView adminCategoriaView = new AdminCategoriaView();
    AdminProdutoView adminProdutoView = new AdminProdutoView();
    VendaDao vendaDao = new VendaDao();

    public void iniciar() {
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
                    // módulo reajuste de preço em construção...
                    continue;
                case 5:
                    MenuAdminTemplate.listarVendas(vendaDao.listar());
                    Util.pausar();
                    break;

                default:
                    MenuAdminTemplate.exibirErroOpcaoInvalida();
                    break;
            }
        } while (opcaoPrincipal != 0);
    }
}