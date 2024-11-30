package com.flameksandr.java.academix.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.management.relation.Role;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(unique = true, nullable = false)
    String username;

    @Column(unique = true, nullable = false)
    String email;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    String fullName;

    @Enumerated(EnumType.STRING)
    Role role;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

}
