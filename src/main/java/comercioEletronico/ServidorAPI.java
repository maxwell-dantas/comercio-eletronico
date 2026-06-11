package comercioEletronico;

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import comercioEletronico.view.visitante.VisitanteView;
import comercioEletronico.controllers.CategoriaController;
import comercioEletronico.controllers.ProdutoController;
import comercioEletronico.controllers.PromocaoController;
import comercioEletronico.controllers.ClienteController;
import comercioEletronico.controllers.AuthController;
import comercioEletronico.controllers.CarrinhoController;
import comercioEletronico.controllers.VendaController;

public class ServidorAPI {
    public static void main(String[] args) {
        // garante que o servidor sempre inicializará com uma conta admin padrão caso a lista de usuários esteja vazia
        VisitanteView.inicializarSistema();

        // configurando o Jackson para suportar LocalDateTime
        ObjectMapper formatadorJson = new ObjectMapper();
        formatadorJson.registerModule(new JavaTimeModule());
        formatadorJson.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // iniciando o servidor e injetando o formatador customizado
        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson(formatadorJson, false));
        }).start(8080);

        // HEALTH CHECK
        app.get("/health", contexto -> {
            contexto.contentType("application/json");
            contexto.result("{\"status\": \"online\", \"servico\": \"API Comercio Eletronico\"}");
        });

        // REGISTRO DE ROTAS
        CategoriaController.registrarRotas(app);
        ProdutoController.registrarRotas(app);
        ClienteController.registrarRotas(app);
        AuthController.registrarRotas(app);
        CarrinhoController.registrarRotas(app);
        VendaController.registrarRotas(app);
        PromocaoController.registrarRotas(app);

        System.out.println("Servidor iniciado com sucesso na porta 8080!");
    }
}