package comercioEletronico.view.admin;

import comercioEletronico.model.entities.Promocao;
import comercioEletronico.model.dao.PromocaoDao;
import java.util.ArrayList;

public class AdminPromocaoView {

    private static PromocaoDao promocaoDao = new PromocaoDao();

    public static ArrayList<Promocao> obterPromocoes() {
        return promocaoDao.listar();
    }

    public static void inserir(Promocao novaPromocao) {
        // validação básica da entidade (Data Fim >= Data Início)
        novaPromocao.setDataFim(novaPromocao.getDataFim());

        // verifica se já existe promoção ativa/agendada com sobreposição
        boolean conflitoDePeriodo = promocaoDao.listar().stream()
                .anyMatch(promocaoExistente ->
                        promocaoExistente.getIdCategoria() == novaPromocao.getIdCategoria() &&
                                (!novaPromocao.getDataInicio().isAfter(promocaoExistente.getDataFim()) &&
                                        !novaPromocao.getDataFim().isBefore(promocaoExistente.getDataInicio()))
                );

        if (conflitoDePeriodo) {
            throw new IllegalArgumentException("Erro de validação: Esta categoria já possui uma promoção ativa ou agendada que coincide com este período.");
        }

        promocaoDao.inserir(novaPromocao);
    }

    public static void remover(int idPromocao) {
        promocaoDao.remover(idPromocao);
    }
}