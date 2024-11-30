package com.flameksandr.java.academix.service;

import com.flameksandr.java.academix.dto.UserDTO;
import com.flameksandr.java.academix.exception.UserNotFoundException;
import com.flameksandr.java.academix.model.User;
import com.flameksandr.java.academix.model.Role;
import com.flameksandr.java.academix.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

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
        userDTO = new UserDTO(1, "john_doe", "john@example.com", "password123", "John Doe", Role.STUDENT);
        user = new User(1, "john_doe", "john@example.com", "password123", "John Doe", Role.STUDENT, null, null);
    }

    @Test
    void testCreateUser_Success() {
        // Given
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(false);
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        // When
        User createdUser = userService.createUser(userDTO);

        // Then
        assertNotNull(createdUser);
        assertEquals(userDTO.getUsername(), createdUser.getUsername());
        assertEquals(userDTO.getEmail(), createdUser.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateUser_EmailAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDTO));
        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateUser_UsernameAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDTO));
        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testGetUserByEmail_Success() {
        // Given
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(java.util.Optional.of(user));

        // When
        User foundUser = userService.getUserByEmail(userDTO.getEmail());

        // Then
        assertNotNull(foundUser);
        assertEquals(userDTO.getEmail(), foundUser.getEmail());
        verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
    }

    @Test
    void testGetUserByEmail_NotFound() {
        // Given
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(java.util.Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail(userDTO.getEmail()));
        assertEquals("User not found with email: " + userDTO.getEmail(), exception.getMessage());
        verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
    }

    @Test
    void testGetUserByUsername_Success() {
        // Given
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(java.util.Optional.of(user));

        // When
        User foundUser = userService.getUserByUsername(userDTO.getUsername());

        // Then
        assertNotNull(foundUser);
        assertEquals(userDTO.getUsername(), foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername(userDTO.getUsername());
    }

    @Test
    void testGetUserByUsername_NotFound() {
        // Given
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(java.util.Optional.empty());

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername(userDTO.getUsername()));
        assertEquals("User not found with username: " + userDTO.getUsername(), exception.getMessage());
        verify(userRepository, times(1)).findByUsername(userDTO.getUsername());
    }

    @Test
    void testUpdateUser_Success() {
        // Given
        when(userRepository.existsById(userDTO.getId())).thenReturn(true);
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        // When
        User updatedUser = userService.updateUser(userDTO);

        // Then
        assertNotNull(updatedUser);
        assertEquals(userDTO.getId(), updatedUser.getId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUser_NotFound() {
        // Given
        when(userRepository.existsById(userDTO.getId())).thenReturn(false);

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.updateUser(userDTO));
        assertEquals("User not found with id: " + userDTO.getId(), exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testDeleteUser_Success() {
        // Given
        when(userRepository.existsById(userDTO.getId())).thenReturn(true);

        // When
        userService.deleteUser(userDTO.getId());

        // Then
        verify(userRepository, times(1)).deleteById(userDTO.getId());
    }

    @Test
    void testDeleteUser_NotFound() {
        // Given
        when(userRepository.existsById(userDTO.getId())).thenReturn(false);

        // When & Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userDTO.getId()));
        assertEquals("User not found with id: " + userDTO.getId(), exception.getMessage());
        verify(userRepository, never()).deleteById(any());
    }
}
