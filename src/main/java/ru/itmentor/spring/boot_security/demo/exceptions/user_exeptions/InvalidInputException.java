package ru.itmentor.spring.boot_security.demo.exceptions.user_exeptions;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}