package comercioEletronico.model.dao;

import comercioEletronico.model.entities.Categoria;
import java.util.ArrayList;
import com.google.gson.reflect.TypeToken;

public class CategoriaDao extends DaoGenerico<Categoria> {

    public CategoriaDao() {
        super("data/categorias.json", new TypeToken<ArrayList<Categoria>>() {}.getType());
    }

    public boolean isDescricaoDisponivel(String descricao) {
        abrir();
        for (Categoria categoria : lista) {
            if (categoria.getDescricao().equalsIgnoreCase(descricao)) {
                return false;
            }
        }
        return true;
    }
}