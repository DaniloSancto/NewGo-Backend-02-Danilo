package dev.danilosantos.application.dto;

public class ProductUpdateDto {
    private String description;
    private Double price;
    private Double quantity;
    private Double minStorage;

    public ProductUpdateDto() {
    }

    public ProductUpdateDto(String description, Double price, Double quantity, Double minStorage) {
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.minStorage = minStorage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
