package comercioEletronico.api.controllers;

import io.javalin.Javalin;
import comercioEletronico.model.entities.Cliente;
import comercioEletronico.view.visitante.VisitanteView;

public class AuthController {

    public static void registrarRotas(Javalin app) {

        // login - retorna o objeto completo do cliente
        app.post("/login", contexto -> {
            try {
                Cliente credenciais = contexto.bodyAsClass(Cliente.class);
                Cliente clienteLogado = VisitanteView.entrar(credenciais.getEmail(), credenciais.getSenha());
                contexto.status(200).json(clienteLogado);
            } catch (IllegalArgumentException e) {
                contexto.status(401).result(e.getMessage());
            } catch (Exception e) {
                contexto.status(500).result("Erro no servidor ao processar o login.");
            }
        });

        // cadastro
        app.post("/cadastro", contexto -> {
            try {
                Cliente novoCliente = contexto.bodyAsClass(Cliente.class);
                VisitanteView.criarConta(
                        novoCliente.getNome(),
                        novoCliente.getTelefone(),
                        novoCliente.getEmail(),
                        novoCliente.getSenha(),
                        novoCliente.getIdFuncao()
                );
                contexto.status(201).result("Conta criada com sucesso!");
            } catch (IllegalArgumentException e) {
                contexto.status(400).result(e.getMessage());
            }
        });
    }
}