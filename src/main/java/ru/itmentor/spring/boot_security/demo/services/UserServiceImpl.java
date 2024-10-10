package ru.itmentor.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.exceptions.UserNotFoundException;
import ru.itmentor.spring.boot_security.demo.models.Role;
import ru.itmentor.spring.boot_security.demo.models.User;
import ru.itmentor.spring.boot_security.demo.repositories.RoleRepository;
import ru.itmentor.spring.boot_security.demo.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class  UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository; // Добавление RoleRepository для работы с ролями
    private final PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository; // Инициализация RoleRepository
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    @Override
    @Transactional
    public void save(User user) {
        if (user.getId() != null && userRepository.existsById(user.getId())) {
            // Логика обновления пользователя
            User existingUser = findById(user.getId());
            if (!user.getPassword().equals(existingUser.getPassword())) {
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            }

            // Убедитесь, что вы обновляете роли и другие поля, если нужно
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setAge(user.getAge());
            existingUser.setRoles(user.getRoles());
            userRepository.save(existingUser);
        } else {
            // Логика сохранения нового пользователя
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Email уже используется");
            }
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email уже используется");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void register(User user, List<Role> roles) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email уже используется");
        }
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Пользователь должен иметь хотя бы одну роль");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(roles));
        userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void update(User user) {
        User existingUser = findById(user.getId());
        if (!user.getPassword().equals(existingUser.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<User> user = userRepository.findByName(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return user.get();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден по email"));
    }
}