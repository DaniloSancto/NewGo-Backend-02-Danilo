package dev.danilosantos.application.dto;

import java.util.Date;
import java.util.UUID;

public class ProductBatchResponseDto {
    private UUID hash;
    private String nome;
    private String descricao;
    private String ean13;
    private Double preco;
    private Double quantidade;
    private Double estoqueMin;
    private Date dtCreate;
    private Date dtUpdate;
    private Boolean lAtivo;
    private String status;
    private String message;

    public ProductBatchResponseDto() {
    }

    public ProductBatchResponseDto(UUID hash, String nome, String descricao, String ean13, Double preco, Double quantidade, Double estoqueMin, Date dtCreate, Date dtUpdate, Boolean lAtivo, String status, String message) {
        this.hash = hash;
        this.nome = nome;
        this.descricao = descricao;
        this.ean13 = ean13;
        this.preco = preco;
        this.quantidade = quantidade;
        this.estoqueMin = estoqueMin;
        this.dtCreate = dtCreate;
        this.dtUpdate = dtUpdate;
        this.lAtivo = lAtivo;
        this.status = status;
        this.message = message;
    }

    public UUID getHash() {
        return hash;
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

    public Date getDtCreate() {
        return dtCreate;
    }

    public Date getDtUpdate() {
        return dtUpdate;
    }

    public Boolean getlAtivo() {
        return lAtivo;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
