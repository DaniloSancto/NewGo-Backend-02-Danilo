package dev.danilosantos.infrastructure;

import java.util.Date;
import java.util.UUID;

public class Product {
    private Long id;
    private UUID hash;
    private String name;
    private String description;
    private String ean13;
    private Double price;
    private Double quantity;
    private Double minStorage;
    private Date dtCreate;
    private Date dtUpdate;

    public Product() {
    }

    public Product(Long id, UUID hash, String name, String description, String ean13, Double price, Double quantity, Double minStorage, Date dtCreate, Date dtUpdate) {
        this.id = id;
        this.hash = hash;
        this.name = name;
        this.description = description;
        this.ean13 = ean13;
        this.price = price;
        this.quantity = quantity;
        this.minStorage = minStorage;
        this.dtCreate = dtCreate;
        this.dtUpdate = dtUpdate;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEan13() {
        return ean13;
    }

    public void setEan13(String ean13) {
        this.ean13 = ean13;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getMinStorage() {
        return minStorage;
    }

    public void setMinStorage(Double minStorage) {
        this.minStorage = minStorage;
    }

    public Date getDtCreate() {
        return dtCreate;
    }

    public void setDtCreate(Date dtCreate) {
        this.dtCreate = dtCreate;
    }

    public Date getDtUpdate() {
        return dtUpdate;
    }

    public void setDtUpdate(Date dtUpdate) {
        this.dtUpdate = dtUpdate;
    }
}
