package dev.danilosantos.application.dto;

import java.util.UUID;

public class ProductUpdatePriceBatchDto {
    private String hash;
    private String operacao;
    private Double porcentagem;

    public ProductUpdatePriceBatchDto() {
    }

    public ProductUpdatePriceBatchDto(String hash, String operacao, Double porcentagem) {
        this.hash = hash;
        this.operacao = operacao;
        this.porcentagem = porcentagem;
    }

    public String getHash() {
        return hash;
    }

    public String getOperacao() {
        return operacao;
    }

    public Double getPorcentagem() {
        return porcentagem;
    }
}
