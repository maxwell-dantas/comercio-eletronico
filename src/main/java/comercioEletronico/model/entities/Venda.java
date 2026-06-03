package comercioEletronico.model.entities;

import java.time.LocalDateTime;

public class Venda implements Identificavel {
    private int id;
    private LocalDateTime data = null;
    private boolean carrinho = true;
    private double total;
    private int idCliente;

    public Venda() {}

    public Venda(int idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        if (this.id != 0) {
            throw new IllegalStateException("Violação de Segurança: O ID de uma venda não pode ser modificado após ser gerado!");
        }
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public boolean getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(boolean carrinho) {
        this.carrinho = carrinho;

        if (!carrinho) {
            this.data = LocalDateTime.now();
        }
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        if (total < 0) {
            throw new IllegalArgumentException("Erro de validação: o valor total da venda não pode ser negativo.");
        }
        this.total = total;
    }

    public int getIdCliente() {
        return idCliente;
    }

    @Override
    public String toString() {
        return "Total: R$ " + String.format("%.2f", total) + " - Data: " + data;
    }
}