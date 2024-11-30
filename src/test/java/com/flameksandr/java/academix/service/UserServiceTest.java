package com.flameksandr.java.academix.service;

import com.flameksandr.java.academix.model.Role;
import com.flameksandr.java.academix.model.User;
import com.flameksandr.java.academix.repository.UserRepository;
import com.flameksandr.java.academix.service.UserService;
import com.flameksandr.java.academix.exception.DuplicateEmailException;
import com.flameksandr.java.academix.exception.DuplicateUsernameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Инициализация mock объектов

        // Инициализация тестовых пользователей
        user1 = new User("johndoe", "johndoe@example.com", "password123", "John Doe", Role.STUDENT);
        user2 = new User("janedoe", "johndoe@example.com", "password456", "Jane Doe", Role.TEACHER);
    }

    @Test
    public void testCreateUserWithDuplicateEmail() {
        // Подготовка mock поведения репозитория
        when(userRepository.existsByEmail(user2.getEmail())).thenReturn(true); // Email уже существует

        // Проверяем, что выбрасывается исключение, если email уже существует
        assertThrows(DuplicateEmailException.class, () -> userService.createUser(user2));

        // Проверяем, что метод existsByEmail был вызван с правильным аргументом
        verify(userRepository).existsByEmail(user2.getEmail());

        // Убедимся, что метод save не был вызван, так как пользователю не удалось быть сохраненным
        verify(userRepository, never()).save(user2);
    }

    @Test
    public void testCreateUserWithDuplicateUsername() {
        // Подготовка mock поведения репозитория
        when(userRepository.existsByUsername(user2.getUsername())).thenReturn(true); // Username уже существует

        // Проверяем, что выбрасывается исключение, если username уже существует
        assertThrows(DuplicateUsernameException.class, () -> userService.createUser(user2));

        // Проверяем, что метод existsByUsername был вызван с правильным аргументом
        verify(userRepository).existsByUsername(user2.getUsername());

        // Убедимся, что метод save не был вызван, так как пользователю не удалось быть сохраненным
        verify(userRepository, never()).save(user2);
    }

    @Test
    public void testCreateUserWithUniqueEmailAndUsername() {
        // Подготовка mock поведения репозитория
        when(userRepository.existsByEmail(user1.getEmail())).thenReturn(false); // Email не существует
        when(userRepository.existsByUsername(user1.getUsername())).thenReturn(false); // Username не существует

        // Создаем нового пользователя и проверяем, что он сохраняется в репозитории
        userService.createUser(user1);

        // Проверяем, что метод existsByEmail был вызван с правильным аргументом
        verify(userRepository).existsByEmail(user1.getEmail());

        // Проверяем, что метод existsByUsername был вызван с правильным аргументом
        verify(userRepository).existsByUsername(user1.getUsername());

        // Убедимся, что метод save был вызван, так как пользователь должен быть сохранен
        verify(userRepository).save(user1);
    }
}
