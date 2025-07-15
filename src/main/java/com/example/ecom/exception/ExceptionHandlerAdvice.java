package com.example.ecom.exception;

import com.example.ecom.exception.user.UserExistException;
import com.example.ecom.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlerAdvice {
//
//    @ExceptionHandler(value = UserNotFoundException.class)
//    //@ResponseStatus(HttpStatus.OK)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public @ResponseBody ErrorResponse handleException(UserNotFoundException ex) {
//        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
//    }
//
//    @ExceptionHandler(value = UserExistException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public @ResponseBody ErrorResponse handleException(UserExistException ex) {
//        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
//    }
//
//    @ExceptionHandler(value = RuntimeException.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public @ResponseBody ErrorResponse handleException(RuntimeException ex) {
//        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
//    }
}


