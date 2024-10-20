package ru.itmentor.spring.boot_security.demo.exceptions.user_exeptions;

public class UserNotFoundException extends UserCustomException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
