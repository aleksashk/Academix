package com.flameksandr.java.academix.service;

import com.flameksandr.java.academix.dto.UserDTO;
import com.flameksandr.java.academix.exception.UserNotFoundException;
import com.flameksandr.java.academix.model.User;
import com.flameksandr.java.academix.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Создание нового пользователя.
     * Проверка на уникальность email и username.
     *
     * @param userDTO DTO объекта пользователя.
     * @return Сохраненный пользователь.
     * @throws IllegalArgumentException Если email или username уже существуют в базе.
     */
    public User createUser(@Valid UserDTO userDTO) {
        // Проверка на уникальность email
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Проверка на уникальность username
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Преобразуем DTO в сущность User
        User user = modelMapper.map(userDTO, User.class);

        // Если все проверки прошли, сохраняем пользователя
        return userRepository.save(user);
    }

    /**
     * Получение пользователя по email.
     *
     * @param email Email пользователя.
     * @return Найденный пользователь.
     * @throws UserNotFoundException Если пользователь не найден.
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    /**
     * Получение пользователя по username.
     *
     * @param username Username пользователя.
     * @return Найденный пользователь.
     * @throws UserNotFoundException Если пользователь не найден.
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
    }

    /**
     * Обновление данных пользователя.
     *
     * @param userDTO Обновленный пользователь в виде DTO.
     * @return Обновленный пользователь.
     * @throws UserNotFoundException Если пользователь не найден.
     */
    public User updateUser(@Valid UserDTO userDTO) {
        if (!userRepository.existsById(userDTO.getId())) {
            throw new UserNotFoundException("User not found with id: " + userDTO.getId());
        }

        // Преобразуем DTO в сущность User
        User user = modelMapper.map(userDTO, User.class);

        return userRepository.save(user);
    }

    /**
     * Удаление пользователя.
     *
     * @param id Идентификатор пользователя, которого нужно удалить.
     * @throws UserNotFoundException Если пользователь с данным id не найден.
     */
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
