package com.example.ecom.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {


    private String message;

    private Map<String, List<String>> errors;

    public ErrorResponse(String message, Map<String, List<String>> errors) {
        this.message = message;
        this.errors = errors;
    }

}

