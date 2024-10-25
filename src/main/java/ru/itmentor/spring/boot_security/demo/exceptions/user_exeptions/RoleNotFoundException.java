package ru.itmentor.spring.boot_security.demo.exceptions.user_exeptions;

public class RoleNotFoundException extends RoleCustomException{

    public RoleNotFoundException(String message) {
        super(message);
    }

    public RoleNotFoundException(String message, String message1) {
        super(message, message1);
    }
}
