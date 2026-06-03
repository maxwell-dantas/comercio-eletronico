package comercioEletronico.model.dao;

import comercioEletronico.model.entities.VendaItem;
import java.util.ArrayList;
import com.google.gson.reflect.TypeToken;

public class VendaItemDao extends DaoGenerico<VendaItem> {

    public VendaItemDao() {
        super("data/venda_itens.json", new TypeToken<ArrayList<VendaItem>>() {}.getType());
    }

    public void limparCarrinho(int idVenda) {
        abrir();
        lista.removeIf(item -> item.getIdVenda() == idVenda);
        salvar();
    }

    public VendaItem obterVendaItemProduto(int idVenda, int idProduto) {
        abrir();
        for (VendaItem item : lista) {
            if (item.getIdVenda() == idVenda && item.getIdProduto() == idProduto) {
                return item;
            }
        }
        return null;
    }
}