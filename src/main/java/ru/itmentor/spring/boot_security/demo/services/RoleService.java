package ru.itmentor.spring.boot_security.demo.services;

import ru.itmentor.spring.boot_security.demo.models.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAll();

    Role findByName(String name);

    List<Role> findByIds(List<Long> ids); // Новый метод
}