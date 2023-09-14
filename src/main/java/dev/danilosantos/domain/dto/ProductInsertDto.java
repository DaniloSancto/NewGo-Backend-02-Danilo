package dev.danilosantos.domain.dto;

public class ProductInsertDto {
    private String name;
    private String description;
    private String ean13;
    private Double price;
    private Double quantity;
    private Double minStorage;

    public ProductInsertDto(String name, String description, String ean13, Double price, Double quantity, Double minStorage) {
        this.name = name;
        this.description = description;
        this.ean13 = ean13;
        this.price = price;
        this.quantity = quantity;
        this.minStorage = minStorage;
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
}
