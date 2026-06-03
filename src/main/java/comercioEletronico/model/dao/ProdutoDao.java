package comercioEletronico.model.dao;

import comercioEletronico.model.entities.Produto;
import java.util.ArrayList;
import com.google.gson.reflect.TypeToken;

public class ProdutoDao extends DaoGenerico<Produto> {

    public ProdutoDao() {
        super("data/produtos.json", new TypeToken<ArrayList<Produto>>() {}.getType());
    }

    public boolean isDescricaoDisponivel(String descricao) {
        abrir();
        for (Produto produto : lista) {
            if (produto.getDescricao().equalsIgnoreCase(descricao)) {
                return false;
            }
        }
        return true;
    }

    public void atualizarEstoque(int idProduto, int estoqueBaixado) {
        Produto produto = listarId(idProduto);
        if (produto != null) {
            produto.setEstoque(produto.getEstoque() - estoqueBaixado);
            atualizar(produto);
        }
    }
}