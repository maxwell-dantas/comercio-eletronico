package comercioEletronico.controllers;

import io.javalin.Javalin;
import comercioEletronico.model.entities.Promocao;
import comercioEletronico.model.dao.PromocaoDao;

public class PromocaoController {
    
    private static PromocaoDao promocaoDao = new PromocaoDao();

    public static void registrarRotas(Javalin app) {
        
        // Retorna todas as promoções
        app.get("/promocoes", contexto -> {
            contexto.json(promocaoDao.listar());
        });

        // Cria uma nova promoção com validação de sobreposição
        app.post("/promocoes", contexto -> {
            try {
                Promocao novaPromocao = contexto.bodyAsClass(Promocao.class);
                
                // Força a validação básica da entidade (Data Fim >= Data Início)
                novaPromocao.setDataFim(novaPromocao.getDataFim()); 

                // Verifica se já existe promoção ativa/agendada com sobreposição de datas
                boolean conflitoDePeriodo = promocaoDao.listar().stream()
                    .anyMatch(promocaoExistente -> 
                        promocaoExistente.getIdCategoria() == novaPromocao.getIdCategoria() &&
                        (!novaPromocao.getDataInicio().isAfter(promocaoExistente.getDataFim()) && 
                         !novaPromocao.getDataFim().isBefore(promocaoExistente.getDataInicio()))
                    );

                if (conflitoDePeriodo) {
                    contexto.status(400).result("Erro de validação: Esta categoria já possui uma promoção ativa ou agendada que coincide com este período.");
                    return;
                }

                promocaoDao.inserir(novaPromocao);
                contexto.status(201).result("Promoção ativada com sucesso!");

            } catch (IllegalArgumentException e) {
                contexto.status(400).result(e.getMessage());
            } catch (Exception e) {
                contexto.status(500).result("Erro interno no servidor: " + e.getMessage());
            }
        });

        // Remove uma promoção pelo ID
        app.delete("/promocoes/{id}", contexto -> {
            try {
                int idPromocao = Integer.parseInt(contexto.pathParam("id"));
                promocaoDao.remover(idPromocao);
                contexto.status(200).result("Promoção cancelada com sucesso!");
            } catch (Exception e) {
                contexto.status(500).result("Erro interno ao cancelar promoção: " + e.getMessage());
            }
        });
    }
}