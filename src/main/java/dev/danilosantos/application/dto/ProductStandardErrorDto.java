package dev.danilosantos.application.dto;

public class ProductStandardErrorDto {
    private String hash;
    private String status;
    private String message;

    public ProductStandardErrorDto(String hash, String status, String message) {
        this.hash = hash;
        this.status = status;
        this.message = message;
    }

    public String getHash() {
        return hash;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
