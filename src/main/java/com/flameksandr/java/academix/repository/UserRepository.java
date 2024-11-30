package com.flameksandr.java.academix.repository;

import com.flameksandr.java.academix.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
