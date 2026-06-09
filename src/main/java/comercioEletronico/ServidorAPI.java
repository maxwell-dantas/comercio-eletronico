package comercioEletronico;

import io.javalin.Javalin;
import comercioEletronico.view.visitante.VisitanteView;

import comercioEletronico.api.controllers.CategoriaController;
import comercioEletronico.api.controllers.ProdutoController;
import comercioEletronico.api.controllers.ClienteController;
import comercioEletronico.api.controllers.AuthController;
import comercioEletronico.api.controllers.CarrinhoController;
import comercioEletronico.api.controllers.VendaController;

public class ServidorAPI {
    public static void main(String[] args) {
        // garante que o servidor sempre inicializará com uma conta admin padrão caso a lista de usuários esteja vazia
        VisitanteView.inicializarSistema();

        // INICIANDO O SERVIDOR
        Javalin app = Javalin.create().start(8080);

        // HEALTH CHECK
        app.get("/health", contexto -> {
            contexto.contentType("application/json");
            contexto.result("{\"status\": \"online\", \"servico\": \"API Comercio Eletronico\"}");
        });

        CategoriaController.registrarRotas(app);
        ProdutoController.registrarRotas(app);
        ClienteController.registrarRotas(app);
        AuthController.registrarRotas(app);
        CarrinhoController.registrarRotas(app);
        VendaController.registrarRotas(app);

        System.out.println("Servidor iniciado com sucesso na porta 8080!");
    }
}