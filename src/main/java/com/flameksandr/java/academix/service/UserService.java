package com.flameksandr.java.academix.service;

import com.flameksandr.java.academix.exception.DuplicateEmailException;
import com.flameksandr.java.academix.exception.DuplicateUsernameException;
import com.flameksandr.java.academix.model.User;
import com.flameksandr.java.academix.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user){
        //Проверка на уникальность email
        userRepository.findByEmail(user.getEmail()).ifPresent(existingUser -> {
            throw new DuplicateEmailException("Email " + user.getEmail() + " is already taken");
        });

        //Проверка на уникальность username
        userRepository.findByEmail(user.getUsername()).ifPresent(existingUser -> {
            throw new DuplicateUsernameException("Username " + user.getUsername() + " is already taken");
        });

        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

}
