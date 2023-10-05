package dev.danilosantos.application.dto;

public class ProductUpdateQuantityDto {
    private String hash;
    private Double valor;

    public ProductUpdateQuantityDto() {
    }

    public ProductUpdateQuantityDto (String hash, Double valor) {
        this.hash = hash;
        this.valor = valor;
    }

    public String getHash() {
        return hash;
    }

    public Double getValor() {
        return valor;
    }
}
