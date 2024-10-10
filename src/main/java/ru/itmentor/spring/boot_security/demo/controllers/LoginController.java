package ru.itmentor.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    private final AuthenticationManager authenticationManager;

    @Autowired
    @Lazy
    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public ModelAndView showLoginForm() {

        return new ModelAndView("login");
    }

    @PostMapping("/login")
    public ModelAndView processLogin(@Valid @RequestParam String username, @Valid @RequestParam String password, BindingResult bindingResult, Model model) {
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errors", bindingResult.getAllErrors());
            modelAndView.setViewName("login");
            return modelAndView;
        }

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            if (authentication.isAuthenticated()) {
                modelAndView.setViewName("redirect:/home");
            } else {
                throw new BadCredentialsException("Invalid credentials provided.");
            }
        } catch (AuthenticationException e) {
            modelAndView.addObject("errorMessage", e.getMessage());
            modelAndView.setViewName("login");
        }

        return modelAndView;
    }
}