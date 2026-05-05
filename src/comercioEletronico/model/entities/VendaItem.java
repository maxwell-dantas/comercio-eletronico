package comercioEletronico.model.entities;

public class VendaItem {
    private int id;
    private int quantidade;
    private double preco;
    private int idVenda;
    private int idProduto;

    public VendaItem(int quantidade, double preco, int idVenda, int idProduto) {
        validarEstado(quantidade, preco);
        this.quantidade = quantidade;
        this.preco = preco;
        this.idVenda = idVenda;
        this.idProduto = idProduto;
    }

    public int getId() {
        return id;
    }

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
        validarEstado(quantidade, preco);
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        validarEstado(quantidade, preco);
        this.preco = preco;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    private void validarEstado(int quantidade, double preco) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Erro! A quantidade de itens no carrinho deve ser maior que zero.");
        }
        if (preco < 0) { // Caso haja produtos com desconto (brinde), evitar que o dado seja negativo
            throw new IllegalArgumentException("Erro! O preço do produto não pode ser negativo.");
        }
    }

    @Override
    public String toString() {
        return id + " - " + quantidade + " unidades - R$ " + String.format("%.2f", preco)
                + " - ID venda: " + idVenda + " - ID produto: " + idProduto;
    }
}
