package comercioEletronico.view.admin;

import comercioEletronico.model.entities.Categoria;
import comercioEletronico.model.dao.CategoriaDao;

import java.util.ArrayList;

public class AdminCategoriaView {
    private static CategoriaDao categoriaDao = new CategoriaDao();

    public static ArrayList<Categoria> obterCategorias() {
        if (categoriaDao.listar().isEmpty()) {
            throw new IllegalArgumentException("\nAinda não há nenhuma categoria cadastrada no sistema!");
        }
        return categoriaDao.listar();
    }

    public static void inserir(String descricao) {
        if (!categoriaDao.isDescricaoDisponivel(descricao)) {
            throw new IllegalArgumentException("\nEsta categoria já está cadastrada no sistema.");
        }

        Categoria categoria = new Categoria(descricao);
        categoriaDao.inserir(categoria);
    }

    public static Categoria listarId(int id) {
        return categoriaDao.listarId(id);
    }

    public static void atualizar(Categoria categoria, String descricao) {
        if (!categoria.getDescricao().equalsIgnoreCase(descricao) && !categoriaDao.isDescricaoDisponivel(descricao)) {
            throw new IllegalArgumentException("\nEsta categoria já está cadastrada no sistema.");
        }
        categoriaDao.atualizar(categoria.getId(), descricao);
    }

    public static void remover(Categoria categoria) {
        categoriaDao.remover(categoria.getId());
    }
}