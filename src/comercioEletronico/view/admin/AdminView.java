package comercioEletronico.view.admin;

import comercioEletronico.model.dao.VendaDao;
import comercioEletronico.model.dao.VendaItemDao;
import comercioEletronico.model.entities.Venda;
import comercioEletronico.model.entities.VendaItem;

import java.util.ArrayList;

public class AdminView {
    private static VendaDao vendaDao = new VendaDao();
    private static VendaItemDao vendaItemDao = new VendaItemDao();

    public static ArrayList<Venda> obterVendas() {
        if (vendaDao.listar().isEmpty()) {
            throw new IllegalArgumentException("\nAinda não há nenhuma venda cadastrada no sistema!\n");
        }
        return vendaDao.listar();
    }

    public static ArrayList<VendaItem> obterVendaItems() {
        if (vendaItemDao.listar().isEmpty()) {
            throw new IllegalArgumentException("\nAinda não há itens de venda registrados no sistema!\n");
        }
        return vendaItemDao.listar();
    }
}