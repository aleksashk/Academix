package com.flameksandr.java.academix.service;

import com.flameksandr.java.academix.dto.UserDTO;
import com.flameksandr.java.academix.exception.UserNotFoundException;
import com.flameksandr.java.academix.model.User;
import com.flameksandr.java.academix.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(User user) {
        // Шифруем пароль перед сохранением в БД
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User createUser(@Valid UserDTO userDTO) {
        // Проверка на уникальность email
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            log.warn("Email already exists: {}", userDTO.getEmail());  // Логируем предупреждение
            throw new IllegalArgumentException("Email already exists");
        }

        // Проверка на уникальность username
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            log.warn("Username already exists: {}", userDTO.getUsername());  // Логируем предупреждение
            throw new IllegalArgumentException("Username already exists");
        }

        // Преобразуем DTO в сущность User
        User user = modelMapper.map(userDTO, User.class);

        // Сохраняем пользователя и логируем успешное создание
        User savedUser = userRepository.save(user);
        log.info("Created new user with id: {}", savedUser.getId());  // Логируем успешное создание пользователя

        return savedUser;
    }

    public User getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);  // Логируем получение пользователя по email
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", email);  // Логируем ошибку, если пользователь не найден
                    return new UserNotFoundException("User not found with email: " + email);
                });
    }

    public User getUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);  // Логируем получение пользователя по username
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", username);  // Логируем ошибку, если пользователь не найден
                    return new UserNotFoundException("User not found with username: " + username);
                });
    }

    public User updateUser(@Valid UserDTO userDTO) {
        log.info("Updating user with id: {}", userDTO.getId());  // Логируем обновление пользователя

        if (!userRepository.existsById(userDTO.getId())) {
            log.error("User not found with id: {}", userDTO.getId());  // Логируем ошибку, если пользователь не найден
            throw new UserNotFoundException("User not found with id: " + userDTO.getId());
        }

        // Преобразуем DTO в сущность User
        User user = modelMapper.map(userDTO, User.class);

        // Сохраняем обновленного пользователя
        User updatedUser = userRepository.save(user);
        log.info("User with id {} updated successfully", updatedUser.getId());  // Логируем успешное обновление

        return updatedUser;
    }

    public void deleteUser(Integer id) {
        log.info("Deleting user with id: {}", id);  // Логируем удаление пользователя

        if (!userRepository.existsById(id)) {
            log.error("User not found with id: {}", id);  // Логируем ошибку, если пользователь не найден
            throw new UserNotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
        log.info("User with id {} deleted successfully", id);  // Логируем успешное удаление
    }
}
