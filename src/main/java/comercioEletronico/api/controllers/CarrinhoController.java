package comercioEletronico.api.controllers;

import io.javalin.Javalin;
import comercioEletronico.model.entities.Venda;
import comercioEletronico.model.entities.VendaItem;
import comercioEletronico.view.cliente.ClienteView;
import java.util.ArrayList;

public class CarrinhoController {

    public static void registrarRotas(Javalin app) {

        app.get("/carrinho/{idCliente}", contexto -> {
            try {
                int idCliente = Integer.parseInt(contexto.pathParam("idCliente"));
                Venda venda = ClienteView.buscarCarrinhoAberto(idCliente);

                if (venda == null) {
                    venda = new Venda(idCliente);
                    ClienteView.adicionarVenda(venda);
                }
                contexto.status(200).json(venda);
            } catch (Exception e) {
                contexto.status(500).result("Erro ao processar o carrinho: " + e.getMessage());
            }
        });

        app.post("/carrinho/{idVenda}/itens", contexto -> {
            try {
                int idVenda = Integer.parseInt(contexto.pathParam("idVenda"));
                VendaItem requisicao = contexto.bodyAsClass(VendaItem.class);

                ClienteView.adicionarProduto(idVenda, requisicao.getIdProduto(), requisicao.getQuantidade());
                contexto.status(201).result("Produto adicionado ao carrinho com sucesso!");
            } catch (IllegalArgumentException e) {
                contexto.status(400).result(e.getMessage());
            }
        });

        app.get("/carrinho/{idVenda}/itens", contexto -> {
            try {
                int idVenda = Integer.parseInt(contexto.pathParam("idVenda"));
                ArrayList<VendaItem> itensDesteCarrinho = ClienteView.obterCarrinho(idVenda);
                contexto.status(200).json(itensDesteCarrinho);
            } catch (IllegalArgumentException e) {
                contexto.status(200).json(new ArrayList<VendaItem>());
            }
        });

        app.delete("/carrinho/{idVenda}/limpar", contexto -> {
            try {
                int idVenda = Integer.parseInt(contexto.pathParam("idVenda"));
                ClienteView.limparCarrinho(idVenda);
                contexto.status(200).result("Seu carrinho foi esvaziado.");
            } catch (Exception e) {
                contexto.status(400).result(e.getMessage());
            }
        });

        app.post("/carrinho/{idVenda}/finalizar", contexto -> {
            try {
                int idVenda = Integer.parseInt(contexto.pathParam("idVenda"));
                ClienteView.finalizarCompra(idVenda);
                contexto.status(200).result("Compra finalizada com sucesso! Agradecemos a preferência.");
            } catch (IllegalArgumentException e) {
                contexto.status(400).result(e.getMessage());
            }
        });
    }
}