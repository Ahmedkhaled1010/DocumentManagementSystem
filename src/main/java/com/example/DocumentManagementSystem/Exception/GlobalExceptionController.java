package com.example.DocumentManagementSystem.Exception;

import com.example.DocumentManagementSystem.Exception.Exceptions.*;
import com.example.DocumentManagementSystem.Shared.POJO.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionController extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        String errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        APIResponse<String> response = new APIResponse<>(statusCode.toString(),
                errorMessages, null);
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<APIResponse<String>> exceptionHandler(Exception exception) {
        APIResponse<String> response = new APIResponse<>("500",
                exception.getMessage(), null);
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<APIResponse<String>> handleEmailExists(EmailAlreadyExistsException ex) {
        APIResponse<String> response = new APIResponse<>("400", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<APIResponse<String>> handleUsernameExists(UsernameAlreadyExistsException ex) {
        APIResponse<String> response = new APIResponse<>("400", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NationalIDAlreadyExistsException.class)
    public ResponseEntity<APIResponse<String>> handleNationalIDExists(NationalIDAlreadyExistsException ex) {
        APIResponse<String> response = new APIResponse<>("400", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RegistrationFailedException.class)
    public ResponseEntity<APIResponse<Object>> handleRegistrationFailed(RegistrationFailedException ex) {
        APIResponse<Object> response = new APIResponse<>("401", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<APIResponse<Object>> handleInvalidCredentials(InvalidCredentialsException ex) {
        APIResponse<Object> response = new APIResponse<>("401", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<APIResponse<Object>> handleUserNotFound(UserNotFoundException ex) {
        APIResponse<Object> response = new APIResponse<>("404", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse<Object>> handleNotFound(ResourceNotFoundException ex) {
        APIResponse<Object> response = new APIResponse<>("404", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<APIResponse<String>> handleUnauthorizedAccess(UnauthorizedAccessException ex) {
        APIResponse<String> response = new APIResponse<>("403", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<APIResponse<String>> handleTokenExpired(TokenExpiredException ex) {
        APIResponse<String> response = new APIResponse<>("401", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

}
