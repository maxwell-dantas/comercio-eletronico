package comercioEletronico.view.admin;

import comercioEletronico.model.dao.VendaDao;
import comercioEletronico.model.dao.VendaItemDao;
import comercioEletronico.model.entities.StatusEntrega;
import comercioEletronico.model.entities.Venda;
import comercioEletronico.model.entities.VendaItem;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AdminView {
    private static VendaDao vendaDao = new VendaDao();
    private static VendaItemDao vendaItemDao = new VendaItemDao();

    public static ArrayList<Venda> obterVendas() {
        if (vendaDao.listar().isEmpty()) {
            throw new IllegalArgumentException("\nAinda não há nenhuma venda cadastrada no sistema!");
        }
        return vendaDao.listar();
    }

    public static ArrayList<Venda> obterVendasPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Erro de validação: A data de início não pode ser posterior à data de encerramento.");
        }

        ArrayList<Venda> vendasFiltradas = new ArrayList<>();

        for (Venda venda : vendaDao.listar()) {
            if (!venda.getCarrinho() && venda.getData() != null) {
                LocalDate dataVenda = venda.getData().toLocalDate();

                if (!dataVenda.isBefore(inicio) && !dataVenda.isAfter(fim)) {
                    vendasFiltradas.add(venda);
                }
            }
        }

        if (vendasFiltradas.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma venda encontrada para o período selecionado.");
        }

        return vendasFiltradas;
    }

    public static ArrayList<VendaItem> obterVendaItems() {
        if (vendaItemDao.listar().isEmpty()) {
            throw new IllegalArgumentException("\nAinda não há itens de venda registrados no sistema!");
        }
        return vendaItemDao.listar();
    }

    // --- MÉTODOS DE LOGÍSTICA ---

    public static void alocarEntregador(int idVenda, int idEntregador) {
        Venda venda = vendaDao.listarId(idVenda);

        if (venda == null) {
            throw new IllegalArgumentException("Venda não encontrada.");
        }

        venda.setIdEntregador(idEntregador);
        venda.setStatusEntrega(StatusEntrega.EM_ROTA);

        vendaDao.atualizar(venda);
    }

    public static void confirmarEntrega(int idVenda) {
        Venda venda = vendaDao.listarId(idVenda);

        if (venda == null) {
            throw new IllegalArgumentException("Venda não encontrada.");
        }

        if (venda.getStatusEntrega() != StatusEntrega.EM_ROTA) {
            throw new IllegalArgumentException("Erro de validação: O pedido precisa estar EM ROTA para ser finalizado.");
        }

        // Grava o status e a hora exata da entrega!
        venda.setStatusEntrega(StatusEntrega.ENTREGUE);
        venda.setDataEntrega(LocalDateTime.now());

        vendaDao.atualizar(venda);
    }
}