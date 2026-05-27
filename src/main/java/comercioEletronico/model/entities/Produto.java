package comercioEletronico.model.entities;

public class Produto {
    private int id;
    private String descricao;
    private double preco;
    private int estoque;
    private int idCategoria;

    public Produto(String descricao, double preco, int estoque, int idCategoria) {
        setDescricao(descricao);
        setPreco(preco);
        setEstoque(estoque);
        setIdCategoria(idCategoria);
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
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Erro de validação: a descrição do produto não pode ser vazia.");
        }
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        if (preco <= 0) {
            throw new IllegalArgumentException("Erro de validação: o preço do produto deve ser maior que zero.");
        }
        this.preco = preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        if (estoque < 0) {
            throw new IllegalArgumentException("Erro de validação: o valor do estoque não pode ser negativo.");
        }
        this.estoque = estoque;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        if (idCategoria <= 0) {
            throw new IllegalArgumentException("Erro de validação: o ID da categoria deve ser um número positivo.");
        }
        this.idCategoria = idCategoria;
    }

    @Override
    public String toString() {
        String unidade = (estoque > 1) ? " unidades" : " unidade";
        return id + " - " + descricao + " - R$ " + String.format("%.2f", preco) + " - " + estoque + unidade +" - ID categoria: " + idCategoria;
    }
}