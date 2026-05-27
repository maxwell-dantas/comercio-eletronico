package comercioEletronico;

import io.javalin.Javalin;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ServidorAPI {
    public static void main(String[] args) {
        
        // Inicializa o servidor na porta 8080
        Javalin app = Javalin.create().start(8080);

        // Rota de Health Check (Monitoramento de Saúde da API)
        app.get("/health", contexto -> {
            // Retorna um JSON simples indicando que o servidor está vivo
            contexto.contentType("application/json");
            contexto.result("{\"status\": \"online\", \"servico\": \"API Comercio Eletronico\"}");
        });

        // Rota de Produtos
        app.get("/produtos", contexto -> {
            try {
                String jsonProdutos = Files.readString(Paths.get("data/produtos.json"));
                contexto.contentType("application/json");
                contexto.result(jsonProdutos);
            } catch (Exception e) {
                contexto.status(500).result("Erro ao ler o banco de dados de produtos: " + e.getMessage());
            }
        });

        // Rota de Clientes
        app.get("/clientes", contexto -> {
            try {
                String jsonClientes = Files.readString(Paths.get("data/clientes.json"));
                contexto.contentType("application/json");
                contexto.result(jsonClientes);
            } catch (Exception e) {
                contexto.status(500).result("Erro ao ler o banco de dados de clientes: " + e.getMessage());
            }
        });

        // Rota de Categorias
        app.get("/categorias", contexto -> {
            try {
                String jsonCategorias = Files.readString(Paths.get("data/categorias.json"));
                contexto.contentType("application/json");
                contexto.result(jsonCategorias);
            } catch (Exception e) {
                contexto.status(500).result("Erro ao ler o banco de dados de categorias: " + e.getMessage());
            }
        });

        // Rota de Vendas (Histórico)
        app.get("/vendas", contexto -> {
            try {
                String jsonVendas = Files.readString(Paths.get("data/vendas.json"));
                contexto.contentType("application/json");
                contexto.result(jsonVendas);
            } catch (Exception e) {
                contexto.status(500).result("Erro ao ler o banco de dados de vendas: " + e.getMessage());
            }
        });

        // Rota de Itens da Venda (Detalhes do Carrinho)
        app.get("/venda_itens", contexto -> {
            try {
                String jsonVendaItens = Files.readString(Paths.get("data/venda_itens.json"));
                contexto.contentType("application/json");
                contexto.result(jsonVendaItens);
            } catch (Exception e) {
                contexto.status(500).result("Erro ao ler o banco de dados de itens da venda: " + e.getMessage());
            }
        });

    }
}