package comercioEletronico.api.controllers;

import io.javalin.Javalin;
import comercioEletronico.model.entities.Categoria;
import comercioEletronico.view.admin.AdminCategoriaView;

public class CategoriaController {

    public static void registrarRotas(Javalin app) {

        // Retorna arquivo JSON
        app.get("/categorias", contexto -> {
            try {
                contexto.json(AdminCategoriaView.obterCategorias());
            } catch (IllegalArgumentException e) {
                contexto.status(404).result(e.getMessage());
            }
        });

        // Inserir
        app.post("/categorias", contexto -> {
            try {
                Categoria categoria = contexto.bodyAsClass(Categoria.class);
                AdminCategoriaView.inserir(categoria.getDescricao());
                contexto.status(201).result("Categoria cadastrada com sucesso!");

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
        app.put("/categorias/{id}", contexto -> {
            try {
                int id = Integer.parseInt(contexto.pathParam("id"));
                Categoria categoriaAntiga = AdminCategoriaView.listarId(id);

                if (categoriaAntiga == null) {
                    contexto.status(404).result("Categoria não encontrada.");
                    return;
                }

                Categoria dadosNovos = contexto.bodyAsClass(Categoria.class);
                AdminCategoriaView.atualizar(categoriaAntiga, dadosNovos.getDescricao());
                contexto.status(200).result("Categoria atualizada com sucesso!");

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
        app.delete("/categorias/{id}", contexto -> {
            try {
                int id = Integer.parseInt(contexto.pathParam("id"));
                Categoria categoria = AdminCategoriaView.listarId(id);

                if (categoria == null) {
                    contexto.status(404).result("Categoria não encontrada.");
                    return;
                }

                AdminCategoriaView.remover(categoria);
                contexto.status(200).result("Categoria removida com sucesso!");
            } catch (Exception e) {
                contexto.status(400).result(e.getMessage());
            }
        });
    }
}