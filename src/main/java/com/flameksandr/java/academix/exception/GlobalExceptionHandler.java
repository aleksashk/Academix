package com.flameksandr.java.academix.exception;

import org.springframework.context.MessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    // Обработка исключений, связанных с дублирующимися данными (email, username)
    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDuplicateDataException() {
        // Передаем пустой массив, так как не требуется форматирование сообщения
        String errorMessage = messageSource.getMessage("error.duplicateDataMessage", new Object[0], Locale.getDefault());
        return new ErrorResponse(messageSource.getMessage("error.duplicateData", new Object[0], Locale.getDefault()), errorMessage);
    }

    // Обработка исключений, связанных с ненайденным пользователем
    @ExceptionHandler({UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException() {
        // Передаем пустой массив для получения сообщения
        String errorMessage = messageSource.getMessage("error.userNotFoundMessage", new Object[0], Locale.getDefault());
        return new ErrorResponse(messageSource.getMessage("error.userNotFound", new Object[0], Locale.getDefault()), errorMessage);
    }

    // Обработка других непойманных исключений
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException() {
        // Передаем пустой массив для получения сообщения
        String errorMessage = messageSource.getMessage("error.internalServerErrorMessage", new Object[0], Locale.getDefault());
        return new ErrorResponse(messageSource.getMessage("error.internalServerError", new Object[0], Locale.getDefault()), errorMessage);
    }
}
