package comercioEletronico.model.entities;

public class Categoria {
    private int id;
    private String descricao;

    public Categoria(String descricao) {
        validarEstado(descricao);
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        validarEstado(descricao);
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (this.id != 0) {
            throw new IllegalStateException("Violação de Segurança: O ID de uma categoria não pode ser modificado após ser gerado!");
        }
        this.id = id;
    }

    private void validarEstado(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro de validação: a descrição da categoria não pode ser vazia.");
        }
    }

    @Override
    public String toString() {
        return id + " - " + descricao;
    }
}