package comercioEletronico.model.entities;

public class Categoria implements Identificavel {
    private int id;
    private String descricao;

    public Categoria() {}

    public Categoria(String descricao) {
        setDescricao(descricao);
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro de validação: a descrição da categoria não pode ser vazia.");
        }
        this.descricao = descricao;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        if (this.id != 0) {
            throw new IllegalStateException("Violação de Segurança: O ID de uma categoria não pode ser modificado após ser gerado!");
        }
        this.id = id;
    }

    @Override
    public String toString() {
        return id + " - " + descricao;
    }
}