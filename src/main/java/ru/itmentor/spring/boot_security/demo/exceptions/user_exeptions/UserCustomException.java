package ru.itmentor.spring.boot_security.demo.exceptions.user_exeptions;

public abstract class UserCustomException extends RuntimeException {

    private String message;

    public UserCustomException() {
        super();
    }

    public UserCustomException(String message) {
        super(message);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
