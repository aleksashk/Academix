package com.flameksandr.java.academix.repository;

import com.flameksandr.java.academix.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    // Метод для проверки существования пользователя по email
    boolean existsByEmail(String email);

    // Метод для проверки существования пользователя по username
    boolean existsByUsername(String username);
}
