package dev.danilosantos.application.dto;

public class ProductUpdatePriceDto {
    private String hash;
    private String operacao;
    private Double valor;

    public ProductUpdatePriceDto() {
    }

    public ProductUpdatePriceDto(String hash, String operacao, Double valor) {
        this.hash = hash;
        this.operacao = operacao;
        this.valor = valor;
    }

    public String getHash() {
        return hash;
    }

    public String getOperacao() {
        return operacao;
    }

    public Double getValor() {
        return valor;
    }
}
