package ru.itmentor.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.dto.UserDTO;
import ru.itmentor.spring.boot_security.demo.dto.UserResponseDTO;
import ru.itmentor.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDTO findByEmailUserDTO(String email);

    List<User> findAll();

    User findById(Long id);

    void save(User user);

    void delete(Long id);

    void update(User user);

    User findByEmail(String name);


    @Transactional
    void userSaveDTO(UserDTO userDTO);

    @Transactional
    UserDTO userUpdateDTO(UserDTO userDTO);

    List<UserDTO> findAllUserDTOs();

    UserDTO findByIdUserDTO(Long id);

    @Transactional
    void register(User user);

    UserResponseDTO showUser(User user);
}
