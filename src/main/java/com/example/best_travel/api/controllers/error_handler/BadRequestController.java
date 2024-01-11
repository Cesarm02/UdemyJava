package com.example.best_travel.api.controllers.error_handler;

import com.example.best_travel.api.models.response.BaseErrorResponse;
import com.example.best_travel.api.models.response.ErrorResponse;
import com.example.best_travel.util.exceptions.IdNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestController {

    @ExceptionHandler(IdNotFoundException.class)
    public BaseErrorResponse handleIdNotFound(IdNotFoundException idNotFoundException){
        return ErrorResponse.builder()
                .message(idNotFoundException.getMessage())
                .status(HttpStatus.BAD_REQUEST.name())
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .build();
    }

}
