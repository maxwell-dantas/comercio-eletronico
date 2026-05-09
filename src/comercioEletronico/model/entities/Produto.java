package comercioEletronico.model.entities;

public class Produto {
    private int id;
    private String descricao;
    private double preco;
    private int estoque;
    private int idCategoria;

    public Produto(String descricao, double preco, int estoque, int idCategoria) {
        validarEstado(descricao, preco, estoque);
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.idCategoria = idCategoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (this.id != 0) {
            throw new IllegalStateException("Violação de Segurança: O ID de um produto não pode ser modificado após ser gerado!");
        }
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        validarEstado(descricao, preco, estoque);
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        validarEstado(descricao, preco, estoque);
        this.preco = preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        validarEstado(descricao, preco, estoque);
        this.estoque = estoque;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    private void validarEstado(String descricao, double preco, int estoque) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro de validação: a descrição do produto não pode ser vazia.");
        }
        if (preco <= 0) {
            throw new IllegalArgumentException("Erro de validação: o preço do produto deve ser maior que zero.");
        }
        if (estoque < 0) {
            throw new IllegalArgumentException("Erro de validação: o valor do estoque não pode ser negativo.");
        }
    }

    @Override
    public String toString() {
        return id + " - " + descricao + " - R$ " + String.format("%.2f", preco) + " - " + estoque + " unidades - ID categoria: " + idCategoria;
    }
}
