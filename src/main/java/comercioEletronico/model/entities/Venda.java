package comercioEletronico.model.entities;

import java.time.LocalDateTime;

public class Venda implements Identificavel {
    private int id;
    private LocalDateTime data;
    private boolean carrinho;
    private double total;
    private int idCliente;
    
    // Atributos de Logística
    private int idEntregador; 
    private StatusEntrega statusEntrega = StatusEntrega.AGUARDANDO_ENTREGADOR;
    private LocalDateTime dataEntrega; // carimbo de tempo da entrega concluída

    public Venda() {}

    public Venda(int idCliente) {
        this.idCliente = idCliente;
        this.carrinho = true;
        this.total = 0.0;
        this.idEntregador = 0;
        this.statusEntrega = StatusEntrega.AGUARDANDO_ENTREGADOR;
    }

    public Venda(int idCliente, LocalDateTime data, boolean carrinho, double total) {
        this.idCliente = idCliente;
        this.data = data;
        this.carrinho = carrinho;
        this.total = total;
        this.idEntregador = 0;
        this.statusEntrega = StatusEntrega.AGUARDANDO_ENTREGADOR;
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

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public boolean getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(boolean carrinho) {
        this.carrinho = carrinho;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        if (total < 0) {
            throw new IllegalArgumentException("O total da venda não pode ser negativo.");
        }
        this.total = total;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdEntregador() {
        return idEntregador;
    }

    public void setIdEntregador(int idEntregador) {
        this.idEntregador = idEntregador;
    }

    public StatusEntrega getStatusEntrega() {
        return statusEntrega;
    }

    public void setStatusEntrega(StatusEntrega statusEntrega) {
        this.statusEntrega = statusEntrega;
    }

    public LocalDateTime getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(LocalDateTime dataEntrega) {
        this.dataEntrega = dataEntrega;
    }
}