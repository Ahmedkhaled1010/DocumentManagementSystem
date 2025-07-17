package com.example.DocumentManagementSystem.Exception.Exceptions;

public class NationalIDAlreadyExistsException extends RuntimeException {
    public NationalIDAlreadyExistsException(String message) {
        super(message);
    }
}
