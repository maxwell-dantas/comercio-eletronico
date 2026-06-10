package comercioEletronico.api.controllers;

import io.javalin.Javalin;
import comercioEletronico.view.admin.AdminView;
import comercioEletronico.model.entities.Venda;
import comercioEletronico.model.entities.StatusEntrega;
import comercioEletronico.model.dao.VendaDao;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
                
                Venda venda = AdminView.obterVendas().stream()
                        .filter(v -> v.getId() == idVenda)
                        .findFirst()
                        .orElse(null);
                        
                if (venda == null) {
                    contexto.contentType("text/plain; charset=utf-8");
                    contexto.status(404).result("Venda não encontrada.");
                    return;
                }
                
                venda.setIdEntregador(idEntregador);
                venda.setStatusEntrega(StatusEntrega.EM_ROTA);
                
                VendaDao vendaDao = new VendaDao();
                vendaDao.atualizar(venda); 
                
                contexto.status(200).result("Entregador alocado com sucesso e pedido em rota!");
                
            } catch (Exception e) {
                contexto.contentType("text/plain; charset=utf-8");
                contexto.status(400).result("Erro ao alocar: " + e.getMessage());
            }
        });

        app.put("/vendas/{id}/finalizar-entrega", contexto -> {
            try {
                int idVenda = Integer.parseInt(contexto.pathParam("id"));
                
                Venda venda = AdminView.obterVendas().stream()
                        .filter(v -> v.getId() == idVenda)
                        .findFirst()
                        .orElse(null);
                        
                if (venda == null) {
                    contexto.contentType("text/plain; charset=utf-8");
                    contexto.status(404).result("Venda não encontrada.");
                    return;
                }
                
                if (venda.getStatusEntrega() != StatusEntrega.EM_ROTA) {
                    contexto.contentType("text/plain; charset=utf-8");
                    contexto.status(400).result("Erro: O pedido precisa estar EM ROTA para ser finalizado.");
                    return;
                }
                
                // Grava o status e a hora exata da entrega!
                venda.setStatusEntrega(StatusEntrega.ENTREGUE);
                venda.setDataEntrega(LocalDateTime.now());
                
                VendaDao vendaDao = new VendaDao();
                vendaDao.atualizar(venda); 
                
                contexto.status(200).result("Entrega confirmada com sucesso!");
                
            } catch (Exception e) {
                contexto.contentType("text/plain; charset=utf-8");
                contexto.status(500).result("Erro interno ao confirmar entrega: " + e.getMessage());
            }
        });
    }
}