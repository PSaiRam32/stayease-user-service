package com.stayease.user_service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        logger.error("Handling UserNotFoundException: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<String> handlePropertyNotFound(PropertyNotFoundException ex) {
        logger.error("Handling PropertyNotFoundException: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}