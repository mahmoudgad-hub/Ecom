package com.example.ecom.exception.user;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {

        super(message);
    }
}
