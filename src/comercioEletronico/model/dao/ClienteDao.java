package comercioEletronico.model.dao;

import comercioEletronico.model.entities.Cliente;

import java.util.ArrayList;
import java.lang.reflect.Type;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ClienteDao {
    private static ArrayList<Cliente> listaClientes = new ArrayList<>();
    private static final Gson gson = new Gson();

    public void inserir(Cliente cliente) {
        abrir();
        int idGenerator = 1;
        if (!listaClientes.isEmpty()) {
            idGenerator = listaClientes.getLast().getId() + 1;
        }
        cliente.setId(idGenerator);
        listaClientes.add(cliente);
        salvar();
    }

    public Cliente listarId(int id) {
        abrir();
        for (Cliente cliente : listaClientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }
        return null;
    }

    public boolean isEmailDisponivel(String email) {
        abrir();
        for (Cliente cliente : listaClientes) {
            if (cliente.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }
        return true;
    }

    public int obterIdVisitanteLogin(String email, String senha) {
        abrir();
        for (Cliente cliente : listaClientes) {
            if (cliente.getEmail().equalsIgnoreCase(email) && cliente.getSenha().equals(senha)) {
                return cliente.getId();
            }
        }
        return 0;
    }

    public int obterAdminCliente(int id) {
       Cliente cliente = listarId(id);

       if (cliente == null) {
           return 0; // camada de segurança
       }

       if (cliente.getEmail().equalsIgnoreCase("admin")) {
           return 1; // 1 - significa admin
       }

        return 2; // 2 - significa cliente;
    }

    public void atualizar(int id, String nome, String email, String telefone, String senha) {
        Cliente cliente = listarId(id);
        if (cliente != null) {
            cliente.setNome(nome);
            cliente.setEmail(email);
            cliente.setTelefone(telefone);
            cliente.setSenha(senha);
            salvar();
        }
    }

    public void remover(int id) {
        Cliente cliente = listarId(id);
        if (cliente != null) {
            listaClientes.remove(cliente);
            salvar();
        }
    }

    public ArrayList<Cliente> listar() {
        abrir();
        return listaClientes;
    }

    public void abrir() {
        try {
            // 1. Abre o leitor para o caminho onde o arquivo foi salvo
            FileReader leitor = new FileReader("src/comercioEletronico/data/clientes.json");

            // 2. Verifica o tipo da lista
            Type listaTipo = new TypeToken<ArrayList<Cliente>>() {
            }.getType();

            listaClientes = gson.fromJson(leitor, listaTipo); // 3. Leitura de arquivo

            if (listaClientes == null) { // camada de segurança extra (caso o usuário não insira nada)
                listaClientes = new ArrayList<>();
            }

            leitor.close();  // 4. fecha o leitor

        } catch (IOException e) {
            // Caso o arquivo não exista ainda, cria uma lista vazia para evitar erros (o método gson.fromJson)
            listaClientes = new ArrayList<>();
        }
    }

    public void salvar() {
        try {
            // 1. Abertura de arquivo
            FileWriter escritor = new FileWriter("src/comercioEletronico/data/clientes.json");

            // 2. Converte a lista de objetos em texto Json
            String textoJson = gson.toJson(listaClientes);

            // 3. Escreve o texto no arquivo
            escritor.write(textoJson);

            // 4. Fecha o arquivo para confirmar a gravação
            escritor.close();

        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao salvar o arquivo: " + e.getMessage());
        }
    }
}