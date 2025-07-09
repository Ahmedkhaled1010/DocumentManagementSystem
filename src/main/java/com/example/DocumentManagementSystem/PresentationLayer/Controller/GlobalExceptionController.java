package com.example.DocumentManagementSystem.PresentationLayer.Controller;

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

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionController extends ResponseEntityExceptionHandler {

@Override
protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                              HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
    APIResponse<String> response = new APIResponse<>(statusCode.toString(),
            ex.getBindingResult().toString(),null);
    return new ResponseEntity(response, HttpStatus.BAD_REQUEST);

}
    @ExceptionHandler({Exception.class})
    public ResponseEntity<APIResponse<String>> exceptionHandler(Exception exception){
        APIResponse<String> response = new APIResponse<>("500",
                exception.getMessage(),null);
        return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
