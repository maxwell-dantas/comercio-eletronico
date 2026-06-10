package comercioEletronico.api.controllers;

import io.javalin.Javalin;
import comercioEletronico.model.entities.Produto;
import comercioEletronico.view.admin.AdminProdutoView;

public class ProdutoController {

    public static void registrarRotas(Javalin app) {

        // Retorna arquivo JSON
        app.get("/produtos", contexto -> {
            try {
                contexto.json(AdminProdutoView.obterProdutos());
            } catch (IllegalArgumentException e) {
                contexto.status(404).result(e.getMessage());
            }
        });

        // Inserir
        app.post("/produtos", contexto -> {
            try {
                Produto produto = contexto.bodyAsClass(Produto.class);
                AdminProdutoView.inserir(
                        produto.getDescricao(),
                        produto.getPreco(),
                        produto.getEstoque(),
                        produto.getIdCategoria(),
                        produto.getImagemBase64()
                );
                contexto.status(201).result("Produto cadastrado com sucesso!");

            } catch (IllegalArgumentException e) {
                contexto.status(400).result(e.getMessage());

            } catch (Exception e) {
                Throwable causa = e;
                while (causa != null) {
                    if (causa instanceof IllegalArgumentException) {
                        contexto.status(400).result(causa.getMessage());
                        return;
                    }
                    causa = causa.getCause();
                }
                contexto.status(500).result("Erro interno no servidor: " + e.getMessage());
            }
        });

        // Atualizar
        app.put("/produtos/{id}", contexto -> {
            try {
                int id = Integer.parseInt(contexto.pathParam("id"));
                Produto produtoAntigo = AdminProdutoView.listarId(id);

                if (produtoAntigo == null) {
                    contexto.status(404).result("Produto não encontrado.");
                    return;
                }

                Produto dadosNovos = contexto.bodyAsClass(Produto.class);
                AdminProdutoView.atualizar(
                        produtoAntigo,
                        dadosNovos.getDescricao(),
                        dadosNovos.getPreco(),
                        dadosNovos.getEstoque(),
                        dadosNovos.getIdCategoria(),
                        dadosNovos.getImagemBase64()
                );
                contexto.status(200).result("Produto atualizado com sucesso!");

            } catch (IllegalArgumentException e) {
                contexto.status(400).result(e.getMessage());

            } catch (Exception e) {
                Throwable causa = e;
                while (causa != null) {
                    if (causa instanceof IllegalArgumentException) {
                        contexto.status(400).result(causa.getMessage());
                        return;
                    }
                    causa = causa.getCause();
                }
                contexto.status(500).result("Erro interno no servidor: " + e.getMessage());
            }
        });

        // Deletar
        app.delete("/produtos/{id}", contexto -> {
            try {
                int id = Integer.parseInt(contexto.pathParam("id"));
                Produto produto = AdminProdutoView.listarId(id);

                if (produto == null) {
                    contexto.status(404).result("Produto não encontrado.");
                    return;
                }

                AdminProdutoView.remover(produto);
                contexto.status(200).result("Produto removido com sucesso!");
            } catch (Exception e) {
                contexto.status(400).result(e.getMessage());
            }
        });

        // Rota de Aumento
        app.put("/produtos/categoria/{id}/aumento", contexto -> {
            try {
                int idCategoria = Integer.parseInt(contexto.pathParam("id"));
                double porcentagem = Double.parseDouble(contexto.queryParam("porcentagem"));
                
                AdminProdutoView.aplicarAumento(idCategoria, porcentagem);
                contexto.status(200).result("Aumento aplicado com sucesso para a categoria!");
                
            } catch (IllegalArgumentException e) {
                contexto.status(400).result(e.getMessage());
            } catch (Exception e) {
                contexto.status(500).result("Erro interno: " + e.getMessage());
            }
        });
    }
}