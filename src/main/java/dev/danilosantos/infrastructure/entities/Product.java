package dev.danilosantos.infrastructure.entities;

import java.util.Date;
import java.util.UUID;

public class Product {
    private Long id;
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

    public Product() {
    }

    public Product(String descricao, Double preco, Double quantidade, Double estoqueMin) {
        this.descricao = descricao;
        this.preco = preco;
        this.quantidade = quantidade;
        this.estoqueMin = estoqueMin;
    }

    public Product(String nome, String descricao, String ean13, Double preco, Double quantidade, Double estoqueMin) {
        this.nome = nome;
        this.descricao = descricao;
        this.ean13 = ean13;
        this.preco = preco;
        this.quantidade = quantidade;
        this.estoqueMin = estoqueMin;
    }

    public Product(Long id, UUID hash, String nome, String descricao, String ean13, Double preco, Double quantidade, Double estoqueMin, Date dtCreate, Date dtUpdate, Boolean lAtivo) {
        this.id = id;
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
    }

    public Product(UUID hash, String nome, String descricao, String ean13, Double preco, Double quantidade, Double estoqueMin, Date dtCreate, Date dtUpdate, Boolean lAtivo) {
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
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getHash() {
        return hash;
    }

    public void setHash(UUID hash) {
        this.hash = hash;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEan13() {
        return ean13;
    }

    public void setEan13(String ean13) {
        this.ean13 = ean13;
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

    public Date getDtCreate() { return dtCreate; }

    public void setDtCreate(Date dtCreate) {
        this.dtCreate = dtCreate;
    }

    public Date getDtUpdate() {
        return dtUpdate;
    }

    public void setDtUpdate(Date dtUpdate) {
        this.dtUpdate = dtUpdate;
    }

    public Boolean getLAtivo() {
        return lAtivo;
    }

    public void setLAtivo(Boolean lAtivo) {
        this.lAtivo = lAtivo;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", hash=" + hash +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", ean13='" + ean13 + '\'' +
                ", preco=" + preco +
                ", quantidade=" + quantidade +
                ", estoqueMin=" + estoqueMin +
                ", dtCreate=" + dtCreate +
                ", dtUpdate=" + dtUpdate +
                ", lAtivo=" + lAtivo +
                '}';
    }
}
