package comercioEletronico.model.dao;

import comercioEletronico.model.entities.Promocao;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

public class PromocaoDao extends DaoGenerico<Promocao> {

    public PromocaoDao() {
        super("data/promocoes.json", new TypeToken<ArrayList<Promocao>>() {}.getType());
    }
}