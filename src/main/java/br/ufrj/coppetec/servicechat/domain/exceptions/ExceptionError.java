package br.ufrj.coppetec.servicechat.domain.exceptions;

public class ExceptionError {

    private String message;

    public ExceptionError() {
    }

    public ExceptionError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}