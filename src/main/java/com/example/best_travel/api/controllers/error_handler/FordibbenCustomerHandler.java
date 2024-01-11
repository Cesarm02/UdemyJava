package com.example.best_travel.api.controllers.error_handler;

import com.example.best_travel.api.models.response.BaseErrorResponse;
import com.example.best_travel.api.models.response.ErrorResponse;
import com.example.best_travel.util.exceptions.FordibbenCustomerException;
import com.example.best_travel.util.exceptions.IdNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseStatus(HttpStatus.FORBIDDEN)

public class FordibbenCustomerHandler {

    @ExceptionHandler(FordibbenCustomerException.class)
    public BaseErrorResponse handleIdNotFound(FordibbenCustomerException exception){
        return ErrorResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.FORBIDDEN.name())
                .errorCode(HttpStatus.FORBIDDEN.value())
                .build();
    }
}
