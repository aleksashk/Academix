package com.flameksandr.java.academix.exception;


public class UserNotFoundException extends RuntimeException {

    // Конструктор для передачи только сообщения
    public UserNotFoundException(String message) {
        super(message);
    }

    // Конструктор для передачи сообщения и причины (себя в качестве cause)
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

