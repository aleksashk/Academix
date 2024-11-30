package com.flameksandr.java.academix.controller;

import com.flameksandr.java.academix.dto.UserDTO;
import com.flameksandr.java.academix.model.User;
import com.flameksandr.java.academix.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Создание нового пользователя.
     *
     * @param userDTO DTO объекта пользователя.
     * @return Созданный пользователь.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    /**
     * Получение пользователя по email.
     *
     * @param email Email пользователя.
     * @return Найденный пользователь.
     */
    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    /**
     * Получение пользователя по username.
     *
     * @param username Username пользователя.
     * @return Найденный пользователь.
     */
    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    /**
     * Обновление данных пользователя.
     *
     * @param userDTO DTO обновленного пользователя.
     * @return Обновленный пользователь.
     */
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Integer id, @Valid @RequestBody UserDTO userDTO) {
        userDTO.setId(id); // Устанавливаем ID из пути запроса
        return userService.updateUser(userDTO);
    }

    /**
     * Удаление пользователя по id.
     *
     * @param id Идентификатор пользователя.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}

