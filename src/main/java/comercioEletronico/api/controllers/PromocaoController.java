package comercioEletronico.api.controllers;

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

        // Cria uma nova promoção
        app.post("/promocoes", contexto -> {
            try {
                Promocao promocao = contexto.bodyAsClass(Promocao.class);
                
                // Força a validação da regra de negócio (Data Fim > Data Início)
                promocao.setDataFim(promocao.getDataFim()); 

                promocaoDao.inserir(promocao);
                contexto.status(201).result("Promoção ativada com sucesso!");

            } catch (IllegalArgumentException e) {
                contexto.status(400).result(e.getMessage());
            } catch (Exception e) {
                contexto.status(500).result("Erro interno no servidor: " + e.getMessage());
            }
        });
    }
}