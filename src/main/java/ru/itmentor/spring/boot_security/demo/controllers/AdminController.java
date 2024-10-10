package ru.itmentor.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.services.RoleService;
import ru.itmentor.spring.boot_security.demo.services.UserService;
import ru.itmentor.spring.boot_security.demo.util.UserValidator;

import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator userValidator;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, UserValidator userValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/index";
    }

    @GetMapping("/users/{id}/edit")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/admin";
        }
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAll());
        return "admin/edit";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute("user") User user, BindingResult bindingResult, @RequestParam(value = "roleIds", required = false) List<Long> roleIds, Model model) {
        if (validateUserName(user, bindingResult, roleIds, model)) return "/admin/edit";
        userService.save(user);
        return "redirect:/admin";
    }

    private boolean validateUserName(@ModelAttribute("user") User user, BindingResult bindingResult, @RequestParam(value = "roleIds", required = false) List<Long> roleIds, Model model) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());
            return true;
        }
        if (roleIds != null) {
            user.setRoles(new HashSet<>(roleService.findByIds(roleIds)));
        }
        return false;
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());
        return "admin/create";
    }

    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") User user, BindingResult bindingResult, @RequestParam(value = "roleIds", required = false) List<Long> roleIds, Model model) {
        if (validateUserName(user, bindingResult, roleIds, model)) return "/admin/edit";
        userService.update(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
