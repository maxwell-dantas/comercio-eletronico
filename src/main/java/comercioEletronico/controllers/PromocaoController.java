package comercioEletronico.controllers;

import io.javalin.Javalin;
import comercioEletronico.model.entities.Promocao;
import comercioEletronico.view.admin.AdminPromocaoView;

public class PromocaoController {

    public static void registrarRotas(Javalin app) {

        // Retorna arquivo JSON
        app.get("/promocoes", contexto -> {
            contexto.json(AdminPromocaoView.obterPromocoes());
        });

        // Inserir
        app.post("/promocoes", contexto -> {
            try {
                Promocao novaPromocao = contexto.bodyAsClass(Promocao.class);

                AdminPromocaoView.inserir(novaPromocao);
                contexto.status(201).result("Promoção ativada com sucesso!");

            } catch (IllegalArgumentException e) {
                contexto.status(400).result(e.getMessage());
            } catch (Exception e) {
                contexto.status(500).result("Erro interno no servidor: " + e.getMessage());
            }
        });

        // Deletar
        app.delete("/promocoes/{id}", contexto -> {
            try {
                int idPromocao = Integer.parseInt(contexto.pathParam("id"));

                AdminPromocaoView.remover(idPromocao);
                contexto.status(200).result("Promoção cancelada com sucesso!");

            } catch (Exception e) {
                contexto.status(500).result("Erro interno ao cancelar promoção: " + e.getMessage());
            }
        });
    }
}