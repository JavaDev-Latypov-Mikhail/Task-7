package ru.itmentor.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.dto.UserDTO;
import ru.itmentor.spring.boot_security.demo.exceptions.user_exeptions.InvalidInputException;
import ru.itmentor.spring.boot_security.demo.mapper.UserMapper;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.services.RoleService;
import ru.itmentor.spring.boot_security.demo.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService, RoleService roleService,
                           UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAllUserDTOs();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userOpt = userService.findByIdUserDTO(id);
        return new ResponseEntity<>(userOpt, HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidInputException(bindingResult.getAllErrors()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", "))
            );
        }

        User user = userMapper.mapToEntity(userDTO, roleService.findAllRoles());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userService.save(user);

        return new ResponseEntity<>(userMapper.mapToDTO(user), HttpStatus.CREATED);
    }

    @PutMapping("/user")
    public ResponseEntity<UserDTO> updateUser(@RequestBody @Valid UserDTO userDTO) {
        return new ResponseEntity<>(userService.userUpdateDTO(userDTO), HttpStatus.OK);

    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}