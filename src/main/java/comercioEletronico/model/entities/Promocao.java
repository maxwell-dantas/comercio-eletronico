package comercioEletronico.model.entities;

import java.time.LocalDate;

public class Promocao implements Identificavel {
    private int id;
    private int idCategoria;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private double percentualDesconto;

    public Promocao() {}

    public Promocao(int idCategoria, LocalDate dataInicio, LocalDate dataFim, double percentualDesconto) {
        setIdCategoria(idCategoria);
        setDataInicio(dataInicio);
        setDataFim(dataFim);
        setPercentualDesconto(percentualDesconto);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        if (this.id != 0) {
            throw new IllegalStateException("O ID não pode ser modificado após gerado.");
        }
        this.id = id;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        if (idCategoria <= 0) {
            throw new IllegalArgumentException("ID da categoria inválido.");
        }
        this.idCategoria = idCategoria;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        if (dataInicio == null) {
            throw new IllegalArgumentException("A data de início é obrigatória.");
        }
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        if (dataFim == null) {
            throw new IllegalArgumentException("A data de fim é obrigatória.");
        }
        if (this.dataInicio != null && dataFim.isBefore(this.dataInicio)) {
            throw new IllegalArgumentException("A data de fim não pode ser anterior à data de início.");
        }
        this.dataFim = dataFim;
    }

    public double getPercentualDesconto() {
        return percentualDesconto;
    }

    public void setPercentualDesconto(double percentualDesconto) {
        if (percentualDesconto <= 0 || percentualDesconto >= 100) {
            throw new IllegalArgumentException("O percentual de desconto deve estar entre 1% e 99%.");
        }
        this.percentualDesconto = percentualDesconto;
    }
}