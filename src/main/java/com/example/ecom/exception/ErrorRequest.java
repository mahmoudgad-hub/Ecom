package com.example.ecom.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class ErrorRequest {


    private String message;

    private Map<String, List<String>> errors;

    public ErrorRequest(String message, Map<String, List<String>> errors) {
        this.message = message;
        this.errors = errors;
    }

}

