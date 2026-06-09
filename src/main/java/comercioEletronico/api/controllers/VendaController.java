package comercioEletronico.api.controllers;

import io.javalin.Javalin;
import comercioEletronico.view.admin.AdminView;

public class VendaController {

    public static void registrarRotas(Javalin app) {

        // Retorna arquivo JSON referente a vendas
        app.get("/vendas", contexto -> {
            try {
                contexto.json(AdminView.obterVendas());
            } catch (IllegalArgumentException e) {
                contexto.status(404).result(e.getMessage());
            }
        });

        // Retorna arquivo JSON referente aos itens das vendas
        app.get("/venda_itens", contexto -> {
            try {
                contexto.json(AdminView.obterVendaItems());
            } catch (IllegalArgumentException e) {
                contexto.status(404).result(e.getMessage());
            }
        });
    }
}