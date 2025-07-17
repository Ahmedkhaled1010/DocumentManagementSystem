package com.example.DocumentManagementSystem.Exception.Exceptions;

public class UsernameAlreadyExistsException extends RuntimeException    {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
