package ru.itmentor.spring.boot_security.demo.exceptions.user_exeptions;

public class RoleCustomException extends RuntimeException{

        private String message;

    public RoleCustomException(String message) {
        this.message = message;
    }

    public RoleCustomException(String message, String message1) {
        super(message);
        this.message = message1;
    }
}

