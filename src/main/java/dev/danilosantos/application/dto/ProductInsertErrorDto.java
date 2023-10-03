package dev.danilosantos.application.dto;

public class ProductInsertErrorDto {
    private String name;
    private String status;
    private String message;

    public ProductInsertErrorDto(String name, String status, String message) {
        this.name = name;
        this.status = status;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
