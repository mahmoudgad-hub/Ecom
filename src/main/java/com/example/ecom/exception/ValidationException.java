package com.example.ecom.exception;


import java.util.List;
import java.util.Map;

public class ValidationException extends RuntimeException {
    private Map<String, List<String>> errors;

    public ValidationException(String message, Map<String, List<String>> errors) {
        super(message);
        this.errors = errors;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }
}