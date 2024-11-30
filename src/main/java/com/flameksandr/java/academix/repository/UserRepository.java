package com.flameksandr.java.academix.repository;

import com.flameksandr.java.academix.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    // Метод для проверки существования пользователя по email
    boolean existsByEmail(String email);

    // Метод для проверки существования пользователя по username
    boolean existsByUsername(String username);

    // Метод для поиска пользователя по email
    Optional<User> findByEmail(String email);

    // Метод для поиска пользователя по username
    Optional<User> findByUsername(String username);
}
