package comercioEletronico.controllers;

import io.javalin.Javalin;
import comercioEletronico.model.entities.Venda;
import comercioEletronico.model.entities.VendaItem;
import comercioEletronico.view.cliente.ClienteView;
import java.util.ArrayList;

public class CarrinhoController {

    public static void registrarRotas(Javalin app) {

        // Retorna o carrinho aberto do cliente (ou cria um novo, caso ele ainda não tenha)
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

        // Adiciona um novo produto ao carrinho (ou incrementa a quantidade se já existir)
        app.post("/carrinho/{idVenda}/itens", contexto -> {
            try {
                int idVenda = Integer.parseInt(contexto.pathParam("idVenda"));
                VendaItem requisicao = contexto.bodyAsClass(VendaItem.class);

                ClienteView.adicionarProduto(idVenda, requisicao.getIdProduto(), requisicao.getQuantidade());
                contexto.status(201).result("Produto adicionado ao carrinho com sucesso!");

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

        // Lista todos os itens detalhados presentes em um carrinho específico
        app.get("/carrinho/{idVenda}/itens", contexto -> {
            try {
                int idVenda = Integer.parseInt(contexto.pathParam("idVenda"));
                ArrayList<VendaItem> itensDesteCarrinho = ClienteView.obterCarrinho(idVenda);
                contexto.status(200).json(itensDesteCarrinho);
            } catch (IllegalArgumentException e) {
                contexto.status(200).json(new ArrayList<VendaItem>());
            }
        });

        // Esvazia completamente o carrinho (limpeza total)
        app.delete("/carrinho/{idVenda}/limpar", contexto -> {
            try {
                int idVenda = Integer.parseInt(contexto.pathParam("idVenda"));
                ClienteView.limparCarrinho(idVenda);
                contexto.status(200).result("Seu carrinho foi esvaziado.");
            } catch (Exception e) {
                contexto.status(400).result(e.getMessage());
            }
        });

        // Finaliza a compra (dá baixa no estoque físico e envia o pedido para a logística)
        app.post("/carrinho/{idVenda}/finalizar", contexto -> {
            try {
                int idVenda = Integer.parseInt(contexto.pathParam("idVenda"));
                ClienteView.finalizarCompra(idVenda);
                contexto.status(200).result("Compra finalizada com sucesso! Agradecemos a preferência.");
            } catch (IllegalArgumentException e) {
                contexto.status(400).result(e.getMessage());
            }
        });

        // Remove um único item específico do carrinho sem afetar os demais
        app.delete("/carrinho/{idVenda}/itens/{idItem}", contexto -> {
            try {
                int idVenda = Integer.parseInt(contexto.pathParam("idVenda"));
                int idItem = Integer.parseInt(contexto.pathParam("idItem"));

                ClienteView.removerItemCarrinho(idVenda, idItem);
                contexto.status(200).result("Item removido do carrinho com sucesso.");

            } catch (IllegalArgumentException e) {
                contexto.status(400).result(e.getMessage());
            } catch (Exception e) {
                contexto.status(500).result("Erro interno ao remover item: " + e.getMessage());
            }
        });

        // Retorna o histórico de compras já finalizadas de um cliente
        app.get("/cliente/{idCliente}/historico", contexto -> {
            try {
                int idCliente = Integer.parseInt(contexto.pathParam("idCliente"));
                ArrayList<Venda> historico = ClienteView.obterHistoricoCompras(idCliente);
                contexto.status(200).json(historico);

            } catch (IllegalArgumentException e) {
                // Retorna 404 (Not Found) se não houver compras
                contexto.status(404).result(e.getMessage());
            } catch (Exception e) {
                contexto.status(500).result("Erro interno ao buscar histórico: " + e.getMessage());
            }
        });
    }
}