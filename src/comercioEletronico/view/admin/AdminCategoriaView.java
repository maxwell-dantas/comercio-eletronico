package comercioEletronico.view.admin;

import comercioEletronico.model.entities.Categoria;
import comercioEletronico.model.dao.CategoriaDao;
import comercioEletronico.template.admin.AdminCategoriaTemplate;
import comercioEletronico.view.Util;

public class AdminCategoriaView {
    private CategoriaDao categoriaDao = new CategoriaDao();

    public void exibirMenu() {
        Categoria categoria;
        int opcaoCrud;
        int id;
        String dados;

        do {
            opcaoCrud = AdminCategoriaTemplate.obterOpcaoCrud();

            switch (opcaoCrud) {
                case 0:
                    AdminCategoriaTemplate.exibirMensagemSaida();
                    break;

                case 1:
                    if (categoriaDao.listar().isEmpty()) {
                        AdminCategoriaTemplate.exibirMensagemListaVazia();
                        continue;
                    }

                    AdminCategoriaTemplate.listarCategorias(categoriaDao.listar());
                    Util.pausar();
                    break;

                case 2:
                    AdminCategoriaTemplate.exibirCabecalho("Cadastrar");
                    dados = AdminCategoriaTemplate.obterDados();

                    try {
                        categoria = new Categoria(dados);
                        categoriaDao.inserir(categoria);
                        AdminCategoriaTemplate.exibirSucesso("cadastrada");
                        Util.pausar();
                    } catch (IllegalArgumentException e) {
                        AdminCategoriaTemplate.exibirErro(e.getMessage());
                    }
                    break;

                case 3:

                    if (categoriaDao.listar().isEmpty()) {
                        AdminCategoriaTemplate.exibirMensagemListaVazia();
                        continue;
                    }
                    AdminCategoriaTemplate.exibirCabecalho("Atualizar");

                    id = AdminCategoriaTemplate.obterId();
                    categoria = categoriaDao.listarId(id);

                    if (categoria == null) {
                        AdminCategoriaTemplate.exibirErroCategoriaNaoEncontrada();
                        continue;
                    }

                    dados = AdminCategoriaTemplate.obterDados();
                    try {
                        categoriaDao.atualizar(id, dados);
                        AdminCategoriaTemplate.exibirSucesso("atualizada");
                        Util.pausar();
                    } catch (IllegalArgumentException e) {
                        AdminCategoriaTemplate.exibirErro(e.getMessage());
                    }
                    break;

                case 4:

                    if (categoriaDao.listar().isEmpty()) {
                        AdminCategoriaTemplate.exibirMensagemListaVazia();
                        continue;
                    }
                    AdminCategoriaTemplate.exibirCabecalho("Remover");

                    id = AdminCategoriaTemplate.obterId();
                    categoria = categoriaDao.listarId(id);

                    if (categoria == null) {
                        AdminCategoriaTemplate.exibirErroCategoriaNaoEncontrada();
                        continue;
                    }

                    categoriaDao.remover(id);
                    AdminCategoriaTemplate.exibirSucesso("removida");
                    Util.pausar();
                    break;

                default:
                    AdminCategoriaTemplate.exibirErroOpcaoInvalida();
                    break;
            }
        } while (opcaoCrud != 0);
    }
}