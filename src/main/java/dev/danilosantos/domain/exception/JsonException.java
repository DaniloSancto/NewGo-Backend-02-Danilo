package dev.danilosantos.domain.exception;

public class JsonException {
    private String error;

    public JsonException(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
