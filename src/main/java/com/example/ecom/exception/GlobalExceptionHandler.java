package com.example.ecom.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        String mainMessage = ex.getMessage();
        int count = ex.getErrors().size();
        String fullMessage = mainMessage + " (" + count + " error" + (count > 1 ? "s" : "") + ")";
        return ResponseEntity.unprocessableEntity()
                .body(new ErrorResponse(fullMessage, ex.getErrors())); // 422
    }
}