package ru.itmentor.spring.boot_security.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.itmentor.spring.boot_security.demo.dto.UserDTO;
import ru.itmentor.spring.boot_security.demo.dto.UserResponseDTO;
import ru.itmentor.spring.boot_security.demo.models.Role;
import ru.itmentor.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(mapToRoleNames(user.getRoles()))")
    UserDTO mapToDTO(User user);

    @Mapping(target = "roles", expression = "java(mapToRoles(userDTO.getRoles(), roles))")
    User mapToEntity(UserDTO userDTO, Set<Role> roles);

    List<UserDTO> mapToListUserDTO(List<User> userList);

    @Mapping(target = "roles", expression = "java(mapToRoleNames(user.getRoles()))")
    UserResponseDTO mapToResponseDTO(User user);

    default Set<String> mapToRoleNames(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    default Set<Role> mapToRoles(Set<String> roles, Set<Role> existingRoles) {
        return existingRoles.stream()
                .filter(role -> roles.contains(role.getName()))
                .collect(Collectors.toSet());
    }
}