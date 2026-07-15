package com.stayease.user_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.stayease.user_service.dto.Response.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex){
        log.warn("UserNotFoundException: {}", ex.getMessage());
        ErrorResponse error=new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND.value(),LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePropertyNotFound(PropertyNotFoundException ex){
        log.warn("PropertyNotFoundException: {}", ex.getMessage());
        ErrorResponse error=new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND.value(),LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFile(InvalidFileException ex){
        log.warn("InvalidFileException: {}", ex.getMessage());
        ErrorResponse error=new ErrorResponse(ex.getMessage(),HttpStatus.BAD_REQUEST.value(),LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ErrorResponse> handleFileStorageException(FileStorageException ex){
        log.error("FileStorageException: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value(),LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex){
        log.error("Unhandled Exception: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR.value(),LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}