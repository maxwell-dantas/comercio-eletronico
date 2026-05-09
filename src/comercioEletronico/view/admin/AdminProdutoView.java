package comercioEletronico.view.admin;

import comercioEletronico.model.dao.CategoriaDao;
import comercioEletronico.model.dao.ProdutoDao;
import comercioEletronico.model.entities.Produto;
import comercioEletronico.template.admin.AdminCategoriaTemplate;
import comercioEletronico.template.admin.AdminProdutoTemplate;
import comercioEletronico.view.Util;

public class AdminProdutoView {
    private ProdutoDao produtoDao = new ProdutoDao();
    private CategoriaDao categoriaDao = new CategoriaDao();

    public void exibirMenu() {
        Produto produto;
        String[] dados;
        int id;
        double preco;
        int quantidade;
        int idCategoria;
        int opcaoCrud;

        do {
            opcaoCrud = AdminProdutoTemplate.obterOpcaoCrud();

            switch (opcaoCrud) {
                case 0:
                    AdminProdutoTemplate.exibirMensagemSaida();
                    break;

                case 1:
                    if (produtoDao.listar().isEmpty()) {
                        AdminProdutoTemplate.exibirMensagemListaVazia();
                        continue;
                    }

                    AdminProdutoTemplate.listarProdutos(produtoDao.listar());
                    Util.pausar();
                    break;

                case 2:

                    if (categoriaDao.listar().isEmpty()) {
                        AdminProdutoTemplate.exibirErroCategoriaVazia();
                        Util.pausar();
                        continue;
                    }

                    AdminProdutoTemplate.exibirCabecalho("Cadastrar");

                    // [0] - descriçao, [1] - preço, [2] - quantidade, [3] - idCategoria
                    dados = AdminProdutoTemplate.obterDados();

                    try {
                        preco = Double.parseDouble(dados[1]);
                        quantidade = Integer.parseInt(dados[2]);
                        idCategoria = Integer.parseInt(dados[3]);
                    } catch (NumberFormatException e) {
                        AdminProdutoTemplate.exibirErro("Formatação inválida: Digite apenas números válidos para preço, estoque e ID da categoria.");
                        continue;
                    }

                    // verifica se a categoria existe
                    if (categoriaDao.listarId(idCategoria) == null) {
                        AdminCategoriaTemplate.exibirErroCategoriaNaoEncontrada();
                        continue;
                    }

                    try {
                        produto = new Produto(dados[0], preco, quantidade, idCategoria);
                        produtoDao.inserir(produto);
                        AdminProdutoTemplate.exibirSucesso("cadastrado");
                        Util.pausar();
                    } catch (IllegalArgumentException e) {
                        AdminProdutoTemplate.exibirErro(e.getMessage());
                    }
                    break;

                case 3:
                    if (produtoDao.listar().isEmpty()) {
                        AdminProdutoTemplate.exibirMensagemListaVazia();
                        continue;
                    }

                    AdminProdutoTemplate.exibirCabecalho("Atualizar");

                    id = AdminProdutoTemplate.obterId();
                    produto = produtoDao.listarId(id);

                    if (produto == null) {
                        AdminProdutoTemplate.exibirErroProdutoNaoEncontrado();
                        continue;
                    }

                    dados = AdminProdutoTemplate.obterDados();

                    try {
                        preco = Double.parseDouble(dados[1]);
                        quantidade = Integer.parseInt(dados[2]);
                        idCategoria = Integer.parseInt(dados[3]);
                    } catch (NumberFormatException e) {
                        AdminProdutoTemplate.exibirErro("Formatação inválida: Digite apenas números válidos.");
                        continue;
                    }

                    if (categoriaDao.listarId(idCategoria) == null) {
                        AdminCategoriaTemplate.exibirErroCategoriaNaoEncontrada();
                        continue;
                    }

                    try {
                        produtoDao.atualizar(id, dados[0], preco, quantidade, idCategoria);
                        AdminProdutoTemplate.exibirSucesso("atualizado");
                        Util.pausar();
                    } catch (IllegalArgumentException e) {
                        AdminProdutoTemplate.exibirErro(e.getMessage());
                    }
                    break;

                case 4:
                    if (produtoDao.listar().isEmpty()) {
                        AdminProdutoTemplate.exibirMensagemListaVazia();
                        continue;
                    }

                    AdminProdutoTemplate.exibirCabecalho("Remover");

                    id = AdminProdutoTemplate.obterId();
                    produto = produtoDao.listarId(id);

                    if (produto == null) {
                        AdminProdutoTemplate.exibirErroProdutoNaoEncontrado();
                        continue;
                    }

                    produtoDao.remover(id);
                    AdminProdutoTemplate.exibirSucesso("removido");
                    Util.pausar();
                    break;

                default:
                    AdminProdutoTemplate.exibirErroOpcaoInvalida();
                    break;
            }
        } while (opcaoCrud != 0);
    }

    public void reajustarPreco() {
        int opcaoReajuste;
        double obterPorcentagem;

        do {
            opcaoReajuste = AdminProdutoTemplate.obterOpcaoReajustarPreco();

            switch (opcaoReajuste) {
                case 0:
                    AdminProdutoTemplate.exibirMensagemSaida();
                    break;

                case 1:
                    obterPorcentagem = AdminProdutoTemplate.obterPorcentagem(1);

                    if (obterPorcentagem < 0.0) {
                        AdminProdutoTemplate.exibirMensagemErroAumento();
                        continue;
                    }

                    for (Produto produto : produtoDao.listar()) {
                        double novoPreco = produto.getPreco() * (1 + (obterPorcentagem / 100.0));
                        produtoDao.atualizar(produto.getId(), produto.getDescricao(), novoPreco, produto.getEstoque(), produto.getIdCategoria());
                    }

                    AdminProdutoTemplate.exibirSucessoReajuste(1);
                    Util.pausar();
                    break;

                case 2:
                    obterPorcentagem = AdminProdutoTemplate.obterPorcentagem(2);

                    if (obterPorcentagem < 0.0 || obterPorcentagem > 100.0) {
                        AdminProdutoTemplate.exibirMensagemErroDesconto();
                        continue;
                    }

                    for (Produto produto : produtoDao.listar()) {
                        double novoPreco = produto.getPreco() * (1 - (obterPorcentagem / 100.0));
                        produtoDao.atualizar(produto.getId(), produto.getDescricao(), novoPreco, produto.getEstoque(), produto.getIdCategoria());
                    }

                    AdminProdutoTemplate.exibirSucessoReajuste(2);
                    Util.pausar();
                    break;

                default:
                    AdminProdutoTemplate.exibirErroOpcaoInvalida();
                    break;
            }
        } while (opcaoReajuste != 0);
    }
}