package comercioEletronico.model.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Venda {
    private int id;
    private String data = "pendente";
    private boolean carrinho = true;
    private double total;
    private int idCliente;

    public Venda(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (this.id != 0) {
            throw new IllegalStateException("Violação de Segurança: O ID de uma venda não pode ser modificado após ser gerado!");
        }
        this.id = id;
    }

    public String getData() {
        return data;
    }

    private void setData() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        data = LocalDateTime.now().format(formato);
    }

    public boolean getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(boolean carrinho) {
        this.carrinho = carrinho;
        setData();
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        if (total < 0) {
            throw new IllegalArgumentException("Erro! O valor total da venda não pode ser negativo.");
        }
        this.total = total;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public String toString() {
        String statusCarrinho = (carrinho) ? "Carrinho em aberto - " : "Carrinho finalizado - ";
        return statusCarrinho + id + " - R$ " + String.format("%.2f", total) + " - Data da compra: " + data + " - ID cliente: " + idCliente;
    }
}