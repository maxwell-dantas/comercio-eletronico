package comercioEletronico.controllers;

import io.javalin.Javalin;
import comercioEletronico.view.admin.AdminView;

import java.time.LocalDate;

public class VendaController {

    public static void registrarRotas(Javalin app) {

        app.get("/vendas", contexto -> {
            try {
                contexto.json(AdminView.obterVendas());
            } catch (IllegalArgumentException e) {
                contexto.contentType("text/plain; charset=utf-8");
                contexto.status(404).result(e.getMessage());
            }
        });

        app.get("/vendas/relatorio", contexto -> {
            try {
                LocalDate inicio = LocalDate.parse(contexto.queryParam("inicio"));
                LocalDate fim = LocalDate.parse(contexto.queryParam("fim"));

                contexto.json(AdminView.obterVendasPorPeriodo(inicio, fim));

            } catch (IllegalArgumentException e) {
                contexto.contentType("text/plain; charset=utf-8");
                contexto.status(400).result(e.getMessage());
            } catch (Exception e) {
                contexto.contentType("text/plain; charset=utf-8");
                contexto.status(500).result("Erro interno: " + e.getMessage());
            }
        });

        app.get("/venda_itens", contexto -> {
            try {
                contexto.json(AdminView.obterVendaItems());
            } catch (IllegalArgumentException e) {
                contexto.contentType("text/plain; charset=utf-8");
                contexto.status(404).result(e.getMessage());
            }
        });

        app.put("/vendas/{id}/alocar", contexto -> {
            try {
                int idVenda = Integer.parseInt(contexto.pathParam("id"));
                int idEntregador = Integer.parseInt(contexto.queryParam("idEntregador"));

                AdminView.alocarEntregador(idVenda, idEntregador);

                contexto.status(200).result("Entregador alocado com sucesso e pedido em rota!");

            } catch (IllegalArgumentException e) {
                contexto.contentType("text/plain; charset=utf-8");
                contexto.status(400).result(e.getMessage());
            } catch (Exception e) {
                contexto.contentType("text/plain; charset=utf-8");
                contexto.status(500).result("Erro interno ao alocar: " + e.getMessage());
            }
        });

        app.put("/vendas/{id}/finalizar-entrega", contexto -> {
            try {
                int idVenda = Integer.parseInt(contexto.pathParam("id"));

                AdminView.confirmarEntrega(idVenda);

                contexto.status(200).result("Entrega confirmada com sucesso!");

            } catch (IllegalArgumentException e) {
                contexto.contentType("text/plain; charset=utf-8");
                contexto.status(400).result(e.getMessage());
            } catch (Exception e) {
                contexto.contentType("text/plain; charset=utf-8");
                contexto.status(500).result("Erro interno ao confirmar entrega: " + e.getMessage());
            }
        });
    }
}