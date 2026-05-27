package comercioEletronico;

import io.javalin.Javalin;

import comercioEletronico.model.entities.Categoria;
import comercioEletronico.model.entities.Cliente;
import comercioEletronico.model.entities.Produto;
import comercioEletronico.view.admin.AdminCategoriaView;
import comercioEletronico.view.admin.AdminClienteView;
import comercioEletronico.view.admin.AdminProdutoView;
import comercioEletronico.view.admin.AdminView;
import comercioEletronico.view.visitante.VisitanteView;

public class ServidorAPI {
    public static void main(String[] args) {

        // INICIALIZAÇÃO DO SISTEMA
        // Garante que o Admin ("admin", "admin"...) seja criado caso o JSON esteja vazio
        VisitanteView.inicializarSistema();

        // INICIANDO O SERVIDOR
        Javalin app = Javalin.create().start(8080);

        // HEALTH CHECK (verifica o status da API)
        app.get("/health", contexto -> {
            contexto.contentType("application/json");
            contexto.result("{\"status\": \"online\", \"servico\": \"API Comercio Eletronico\"}");
        });

        // ====================================================================
        // ROTAS DE CATEGORIAS
        // ====================================================================

        // Retorna arquivo JSON
        app.get("/categorias", contexto -> {
            try {
                contexto.json(AdminCategoriaView.obterCategorias());
            } catch (IllegalArgumentException e) {
                contexto.status(404).result(e.getMessage());
            }
        });

        // Inserir Categoria
        app.post("/categorias", contexto -> {
            try {
                Categoria categoria = contexto.bodyAsClass(Categoria.class);
                AdminCategoriaView.inserir(categoria.getDescricao());
                contexto.status(201).result("Categoria cadastrada com sucesso!");
            } catch (IllegalArgumentException e) {
                contexto.status(400).result(e.getMessage());
            }
        });

        // Atualizar Categoria
        app.put("/categorias/{id}", contexto -> {
            try {
                int id = Integer.parseInt(contexto.pathParam("id"));
                Categoria categoriaAntiga = AdminCategoriaView.listarId(id);

                if (categoriaAntiga == null) {
                    contexto.status(404).result("Categoria não encontrada.");
                    return;
                }

                Categoria dadosNovos = contexto.bodyAsClass(Categoria.class);
                AdminCategoriaView.atualizar(categoriaAntiga, dadosNovos.getDescricao());
                contexto.status(200).result("Categoria atualizada com sucesso!");
            } catch (IllegalArgumentException e) {
                contexto.status(400).result(e.getMessage());
            }
        });

        // Deletar Categoria
        app.delete("/categorias/{id}", contexto -> {
            try {
                int id = Integer.parseInt(contexto.pathParam("id"));
                Categoria categoria = AdminCategoriaView.listarId(id);

                if (categoria == null) {
                    contexto.status(404).result("Categoria não encontrada.");
                    return;
                }

                AdminCategoriaView.remover(categoria);
                contexto.status(200).result("Categoria removida com sucesso!");
            } catch (Exception e) {
                contexto.status(400).result(e.getMessage());
            }
        });

        // ====================================================================
        // ROTAS DE PRODUTOS
        // ====================================================================

        // Retorna arquivo JSON
        app.get("/produtos", contexto -> {
            try {
                contexto.json(AdminProdutoView.obterProdutos());
            } catch (IllegalArgumentException e) {
                contexto.status(404).result(e.getMessage());
            }
        });

        // Inserir Produto
        app.post("/produtos", contexto -> {
            try {
                Produto produto = contexto.bodyAsClass(Produto.class);
                AdminProdutoView.inserir(
                        produto.getDescricao(),
                        produto.getPreco(),
                        produto.getEstoque(),
                        produto.getIdCategoria()
                );
                contexto.status(201).result("Produto cadastrado com sucesso!");
            } catch (IllegalArgumentException e) {
                contexto.status(400).result(e.getMessage());
            }
        });

        // Atualizar Produto
        app.put("/produtos/{id}", contexto -> {
            try {
                int id = Integer.parseInt(contexto.pathParam("id"));
                Produto produtoAntigo = AdminProdutoView.listarId(id);

                if (produtoAntigo == null) {
                    contexto.status(404).result("Produto não encontrado.");
                    return;
                }

                Produto dadosNovos = contexto.bodyAsClass(Produto.class);
                AdminProdutoView.atualizar(
                        produtoAntigo,
                        dadosNovos.getDescricao(),
                        dadosNovos.getPreco(),
                        dadosNovos.getEstoque(),
                        dadosNovos.getIdCategoria()
                );
                contexto.status(200).result("Produto atualizado com sucesso!");
            } catch (IllegalArgumentException e) {
                contexto.status(400).result(e.getMessage());
            }
        });

        // Deletar Produto
        app.delete("/produtos/{id}", contexto -> {
            try {
                int id = Integer.parseInt(contexto.pathParam("id"));
                Produto produto = AdminProdutoView.listarId(id);

                if (produto == null) {
                    contexto.status(404).result("Produto não encontrado.");
                    return;
                }

                AdminProdutoView.remover(produto);
                contexto.status(200).result("Produto removido com sucesso!");
            } catch (Exception e) {
                contexto.status(400).result(e.getMessage());
            }
        });

        // ====================================================================
        // ROTAS DE CLIENTES
        // ====================================================================

        // Retorna arquivo JSON
        app.get("/clientes", contexto -> {
            try {
                contexto.json(AdminClienteView.obterClientes());
            } catch (IllegalArgumentException e) {
                contexto.status(404).result(e.getMessage());
            }
        });

        // Inserir Cliente
        app.post("/clientes", contexto -> {
            try {
                Cliente cliente = contexto.bodyAsClass(Cliente.class);
                AdminClienteView.inserir(
                        cliente.getNome(),
                        cliente.getTelefone(),
                        cliente.getEmail(),
                        cliente.getSenha()
                );
                contexto.status(201).result("Cliente cadastrado com sucesso!");
            } catch (IllegalArgumentException e) {
                contexto.status(400).result(e.getMessage());
            }
        });

        // Atualizar Cliente
        app.put("/clientes/{id}", contexto -> {
            try {
                int id = Integer.parseInt(contexto.pathParam("id"));

                if (id == 1) {
                    contexto.status(403).result("Os valores do administrador não podem ser alterados!");
                    return;
                }

                Cliente clienteAntigo = AdminClienteView.listarId(id);
                if (clienteAntigo == null) {
                    contexto.status(404).result("Cliente não encontrado.");
                    return;
                }

                Cliente dadosNovos = contexto.bodyAsClass(Cliente.class);
                AdminClienteView.atualizar(
                        clienteAntigo,
                        dadosNovos.getNome(),
                        dadosNovos.getTelefone(),
                        dadosNovos.getEmail(),
                        dadosNovos.getSenha()
                );
                contexto.status(200).result("Cliente atualizado com sucesso!");
            } catch (IllegalArgumentException e) {
                contexto.status(400).result(e.getMessage());
            }
        });

        // Deletar Cliente
        app.delete("/clientes/{id}", contexto -> {
            try {
                int id = Integer.parseInt(contexto.pathParam("id"));

                if (id == 1) {
                    contexto.status(403).result("Não é possível remover o cadastro do administrador!");
                    return;
                }

                Cliente cliente = AdminClienteView.listarId(id);
                if (cliente == null) {
                    contexto.status(404).result("Cliente não encontrado.");
                    return;
                }

                AdminClienteView.remover(cliente);
                contexto.status(200).result("Cliente removido com sucesso!");
            } catch (Exception e) {
                contexto.status(400).result(e.getMessage());
            }
        });

        // ====================================================================
        // SISTEMA DE LOGIN (VISITANTE)
        // ====================================================================

        // Validar credenciais de login
        app.post("/login", contexto -> {
            try {
                Cliente credenciais = contexto.bodyAsClass(Cliente.class);
                int idUsuario = VisitanteView.entrar(credenciais.getEmail(), credenciais.getSenha());

                if (idUsuario == 0) {
                    contexto.status(401).result("E-mail ou senha inválidos!");
                } else {
                    contexto.status(200).json("{\"idUsuario\": " + idUsuario + "}");
                }
            } catch (Exception e) {
                contexto.status(500).result("Erro no servidor ao processar o login.");
            }
        });

        // ====================================================================
        // ROTAS DE VENDAS E ITENS (HISTÓRICO / LEITURA)
        // ====================================================================

        // Retorna arquivo JSON (Vendas gerais)
        app.get("/vendas", contexto -> {
            try {
                contexto.json(AdminView.obterVendas());
            } catch (IllegalArgumentException e) {
                contexto.status(404).result(e.getMessage());
            }
        });

        // Retorna arquivo JSON (Itens das Vendas)
        app.get("/venda_itens", contexto -> {
            try {
                contexto.json(AdminView.obterVendaItems());
            } catch (IllegalArgumentException e) {
                contexto.status(404).result(e.getMessage());
            }
        });

    }
}