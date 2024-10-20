package ru.itmentor.spring.boot_security.demo.services;

import ru.itmentor.spring.boot_security.demo.models.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {

   Set<Role> findRolesByIds(List<Long> roleIds);

   Set<Role> findAllRoles();
}