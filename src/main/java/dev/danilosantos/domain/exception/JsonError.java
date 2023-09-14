package dev.danilosantos.domain.exception;

public class JsonError {
    private String error;

    public JsonError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
