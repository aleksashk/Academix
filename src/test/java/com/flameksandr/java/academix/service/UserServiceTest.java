package com.flameksandr.java.academix.service;

import com.flameksandr.java.academix.dto.UserDTO;
import com.flameksandr.java.academix.exception.UserNotFoundException;
import com.flameksandr.java.academix.model.User;
import com.flameksandr.java.academix.model.Role;
import com.flameksandr.java.academix.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setUp() {
        // Создаем тестовые данные для пользователя и UserDTO
        userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");
        userDTO.setFullName("Test User");

        user = new User();
        user.setId(1);
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFullName("Test User");
    }

    @Test
    void createUser_ShouldReturnUser_WhenEmailAndUsernameAreUnique() {
        // Arrange
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(false);
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = userService.createUser(userDTO);

        // Assert
        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getEmail(), result.getEmail());

        // Verify interactions
        verify(userRepository, times(1)).existsByEmail(userDTO.getEmail());
        verify(userRepository, times(1)).existsByUsername(userDTO.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser_ShouldThrowIllegalArgumentException_WhenEmailExists() {
        // Arrange
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDTO));
        assertEquals("Email already exists", exception.getMessage());

        // Verify interactions
        verify(userRepository, times(1)).existsByEmail(userDTO.getEmail());
        verify(userRepository, never()).existsByUsername(userDTO.getUsername());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowIllegalArgumentException_WhenUsernameExists() {
        // Arrange
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDTO));
        assertEquals("Username already exists", exception.getMessage());

        // Verify interactions
        verify(userRepository, times(1)).existsByEmail(userDTO.getEmail());
        verify(userRepository, times(1)).existsByUsername(userDTO.getUsername());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserByEmail_ShouldReturnUser_WhenUserExists() {
        // Arrange
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(java.util.Optional.of(user));

        // Act
        User result = userService.getUserByEmail(userDTO.getEmail());

        // Assert
        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());

        // Verify interaction
        verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
    }

    @Test
    void getUserByEmail_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(java.util.Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail(userDTO.getEmail()));
        assertEquals("User not found with email: " + userDTO.getEmail(), exception.getMessage());

        // Verify interaction
        verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
    }

    @Test
    void getUserByUsername_ShouldReturnUser_WhenUserExists() {
        // Arrange
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(java.util.Optional.of(user));

        // Act
        User result = userService.getUserByUsername(userDTO.getUsername());

        // Assert
        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());

        // Verify interaction
        verify(userRepository, times(1)).findByUsername(userDTO.getUsername());
    }

    @Test
    void getUserByUsername_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(java.util.Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername(userDTO.getUsername()));
        assertEquals("User not found with username: " + userDTO.getUsername(), exception.getMessage());

        // Verify interaction
        verify(userRepository, times(1)).findByUsername(userDTO.getUsername());
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser_WhenUserExists() {
        // Arrange
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = userService.updateUser(userDTO);

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());

        // Verify interactions
        verify(userRepository, times(1)).existsById(user.getId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.existsById(user.getId())).thenReturn(false);

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.updateUser(userDTO));
        assertEquals("User not found with id: " + user.getId(), exception.getMessage());

        // Verify interactions
        verify(userRepository, times(1)).existsById(user.getId());
        verify(userRepository, never()).save(user);
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        // Arrange
        when(userRepository.existsById(user.getId())).thenReturn(true);

        // Act
        userService.deleteUser(user.getId());

        // Assert
        verify(userRepository, times(1)).existsById(user.getId());
        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void deleteUser_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.existsById(user.getId())).thenReturn(false);

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.deleteUser(user.getId()));
        assertEquals("User not found with id: " + user.getId(), exception.getMessage());

        // Verify interactions
        verify(userRepository, times(1)).existsById(user.getId());
        verify(userRepository, never()).deleteById(user.getId());
    }
}
