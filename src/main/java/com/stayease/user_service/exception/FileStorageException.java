package com.stayease.user_service.exception;


public class FileStorageException extends RuntimeException{
    public FileStorageException(String message){
        super(message);
    }
}