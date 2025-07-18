package com.example.ecom.exception;

import com.example.ecom.Lang.DbMessageSource;
import com.example.ecom.Lang.MessageUtil;
import com.example.ecom.exception.user.UserExistException;
import com.example.ecom.exception.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.Locale;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    String fullMessage =null;
    @Autowired
    private MessageUtil messageUtil;

    @ExceptionHandler(value = UserNotFoundException.class)
    //@ResponseStatus(HttpStatus.OK)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse handleException(UserNotFoundException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(value = UserExistException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public @ResponseBody ErrorResponse handleException(UserExistException ex) {
        return new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorResponse handleException(RuntimeException ex) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorRequest> handleValidationException(ValidationException ex) {
        String mainMessage = ex.getMessage();
        int count = ex.getErrors().size();
        Locale locale = LocaleContextHolder.getLocale();
System.out.println("locale>>>>>"+locale);
        if (locale.equals("en")){
            fullMessage = mainMessage + " (" + count + " error" + (count > 1 ? "s" : "") + ")";
        }else {
          fullMessage = mainMessage + " (" + count +" "+ messageUtil.get("gen.error") + ")";
        }


        return ResponseEntity.unprocessableEntity()
                .body(new ErrorRequest(fullMessage, ex.getErrors())); // 422
    }
}


