package ru.itmentor.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.dto.UserDTO;
import ru.itmentor.spring.boot_security.demo.dto.UserResponseDTO;
import ru.itmentor.spring.boot_security.demo.exceptions.user_exeptions.UserNotFoundException;
import ru.itmentor.spring.boot_security.demo.mapper.UserMapper;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public List<UserDTO> findAllUserDTOs() {
        return userMapper.mapToListUserDTO(userRepository.findAll());
    }

    @Override
    public UserDTO findByIdUserDTO(Long id) {
        return userMapper.mapToDTO(findById(id));
    }

    @Override
    @Transactional
    public void register(User user) {
        encodePassword(user);
        userRepository.save(user);
    }

    @Override
    public UserResponseDTO showUser(User user) {
        return userMapper.mapToResponseDTO(user);
    }

    @Override
    public UserDTO findByEmailUserDTO(String email) {
        return userMapper.mapToDTO(findByEmail(email));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public void save(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }
        encodePassword(user);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(User user) {
        if (userRepository.existsById(user.getId())) {
            User existingUser = findById(user.getId());
            if (!user.getPassword().equals(existingUser.getPassword())) {
                encodePassword(user);
            } else {
                user.setPassword(existingUser.getPassword());
            }
            userRepository.save(user);
        } else {
            throw new UserNotFoundException("User not found with id: " + user.getId());
        }

    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional
    public void userSaveDTO(UserDTO userDTO) {

        save(userMapper.mapToEntity(userDTO, roleService.findAllRoles()));
    }

    @Override
    @Transactional
    public UserDTO userUpdateDTO(UserDTO userDTO) {
        User updatedUser = userMapper.mapToEntity(userDTO, roleService.findAllRoles());
        update(updatedUser);
        return userMapper.mapToDTO(updatedUser);
    }

    @Override

    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return user;
    }

    private void encodePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}