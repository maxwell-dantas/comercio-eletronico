package comercioEletronico.model.entities;

public class VendaItem implements Identificavel {
    private int id;
    private int quantidade;
    private double preco;
    private int idVenda;
    private int idProduto;

    public VendaItem() {}

    public VendaItem(int quantidade, double preco, int idVenda, int idProduto) {
        setQuantidade(quantidade);
        setPreco(preco);
        this.idVenda = idVenda;
        this.idProduto = idProduto;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        if (this.id != 0) {
            throw new IllegalStateException("Violação de Segurança: O ID de um item não pode ser modificado após ser gerado!");
        }
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Erro de validação: a quantidade de itens no carrinho deve ser maior que zero.");
        }
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("Erro de validação: o preço do produto não pode ser negativo.");
        }
        this.preco = preco;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public int getIdProduto() {
        return idProduto;
    }

    @Override
    public String toString() {
        String unidade = (quantidade > 1) ? " unidades" : " unidade";
        return quantidade + unidade + " - Preço unitário: " + String.format("R$ %.2f", preco);
    }
}