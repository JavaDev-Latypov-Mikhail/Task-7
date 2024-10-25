package ru.itmentor.spring.boot_security.demo.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.repositories.UserRepository;

@Component
public class UserValidator implements ConstraintValidator<ValidUser, User> {

    private final UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (userRepository.findByName(user.getUsername()).isPresent()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("User with this name already exists").addConstraintViolation();
            isValid = false;
        }


        return isValid;
    }
}