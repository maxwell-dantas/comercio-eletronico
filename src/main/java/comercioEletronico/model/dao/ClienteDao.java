package comercioEletronico.model.dao;

import comercioEletronico.model.entities.Cliente;
import java.util.ArrayList;
import com.google.gson.reflect.TypeToken;

public class ClienteDao extends DaoGenerico<Cliente> {

    public ClienteDao() {
        super("data/clientes.json", new TypeToken<ArrayList<Cliente>>() {}.getType());
    }

    public boolean isEmailDisponivel(String email) {
        abrir();
        for (Cliente cliente : lista) {
            if (cliente.getEmail().equalsIgnoreCase(email)) {
                return false;
            }
        }
        return true;
    }

    public Cliente obterUsuarioLogin(String email, String senha) {
        abrir();
        for (Cliente cliente : lista) {
            if (cliente.getEmail().equalsIgnoreCase(email) && cliente.getSenha().equals(senha)) {
                return cliente;
            }
        }
        return null;
    }
}