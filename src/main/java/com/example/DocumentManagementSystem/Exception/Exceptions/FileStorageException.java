package com.example.DocumentManagementSystem.Exception.Exceptions;

public class FileStorageException extends RuntimeException {
  public FileStorageException(String message, Throwable cause) {
    super(message, cause);
  }
}