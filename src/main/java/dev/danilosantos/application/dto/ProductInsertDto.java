package dev.danilosantos.application.dto;

public class ProductInsertDto {
    private String nome;
    private String descricao;
    private String ean13;
    private Double preco;
    private Double quantidade;
    private Double estoqueMin;

    public ProductInsertDto(String nome, String descricao, String ean13, Double preco, Double quantidade, Double estoqueMin) {
        this.nome = nome;
        this.descricao = descricao;
        this.ean13 = ean13;
        this.preco = preco;
        this.quantidade = quantidade;
        this.estoqueMin = estoqueMin;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getEan13() {
        return ean13;
    }

    public Double getPreco() {
        return preco;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public Double getEstoqueMin() {
        return estoqueMin;
    }
}
