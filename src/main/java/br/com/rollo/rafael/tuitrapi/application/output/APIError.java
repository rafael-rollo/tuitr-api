package br.com.rollo.rafael.tuitrapi.application.output;

public class APIError {

    private String message;

    public APIError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
