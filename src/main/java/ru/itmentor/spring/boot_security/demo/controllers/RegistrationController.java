package ru.itmentor.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.services.RoleService;
import ru.itmentor.spring.boot_security.demo.services.UserService;

import java.util.HashSet;
import java.util.List;

@Controller
public class RegistrationController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public RegistrationController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());
        return "registration";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user,
                           @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {
        try {
            if (roleIds != null) {
                user.setRoles(new HashSet<>(roleService.findByIds(roleIds)));
            }
            userService.register(user);
        } catch (Exception e) {
            return "registration";
        }
        return "redirect:/login";
    }
}