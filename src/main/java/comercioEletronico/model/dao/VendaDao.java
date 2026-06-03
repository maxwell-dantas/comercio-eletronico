package comercioEletronico.model.dao;

import comercioEletronico.model.entities.Venda;
import java.util.ArrayList;
import com.google.gson.reflect.TypeToken;

public class VendaDao extends DaoGenerico<Venda> {

    public VendaDao() {
        super("data/vendas.json", new TypeToken<ArrayList<Venda>>() {}.getType());
    }
}