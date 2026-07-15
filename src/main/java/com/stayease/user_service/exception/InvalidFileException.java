package com.stayease.user_service.exception;

public class InvalidFileException extends RuntimeException{
    public InvalidFileException(String message){
        super(message);
    }
}