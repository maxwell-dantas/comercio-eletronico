package comercioEletronico.api.controllers;

import io.javalin.Javalin;
import comercioEletronico.model.entities.Cliente;
import comercioEletronico.view.admin.AdminClienteView;

public class ClienteController {

    public static void registrarRotas(Javalin app) {

        // Retorna arquivo JSON
        app.get("/clientes", contexto -> {
            try {
                contexto.json(AdminClienteView.obterClientes());
            } catch (IllegalArgumentException e) {
                contexto.status(404).result(e.getMessage());
            }
        });

        // Inserir
        app.post("/clientes", contexto -> {
            try {
                Cliente cliente = contexto.bodyAsClass(Cliente.class);
                AdminClienteView.inserir(
                        cliente.getNome(),
                        cliente.getTelefone(),
                        cliente.getEmail(),
                        cliente.getSenha(),
                        cliente.getIdFuncao()
                );
                contexto.status(201).result("Cliente cadastrado com sucesso!");

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

        // Atualizar
        app.put("/clientes/{id}", contexto -> {
            try {
                int id = Integer.parseInt(contexto.pathParam("id"));

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
                        dadosNovos.getSenha(),
                        dadosNovos.getIdFuncao()
                );
                contexto.status(200).result("Cliente atualizado com sucesso!");

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

        // Deletar
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
    }
}