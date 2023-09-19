package dev.danilosantos.application.dto;

public class ProductUpdateDto {
    private String descricao;
    private Double preco;
    private Double quantidade;
    private Double estoqueMin;

    public ProductUpdateDto() {
    }

    public ProductUpdateDto(String descricao, Double preco, Double quantidade, Double estoqueMin) {
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
        this.estoqueMin = estoqueMin;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public Double getEstoqueMin() {
        return estoqueMin;
    }

    public void setEstoqueMin(Double estoqueMin) {
        this.estoqueMin = estoqueMin;
    }
}
