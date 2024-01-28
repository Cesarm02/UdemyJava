package com.example.best_travel.api.controllers.error_handler;

import com.example.best_travel.api.models.response.BaseErrorResponse;
import com.example.best_travel.api.models.response.ErrorResponse;
import com.example.best_travel.api.models.response.ErrosResponse;
import com.example.best_travel.util.exceptions.IdNotFoundException;
import com.example.best_travel.util.exceptions.UsernameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestController {

    @ExceptionHandler({IdNotFoundException.class, UsernameNotFoundException.class })
    public BaseErrorResponse handleIdNotFound(RuntimeException exception){
        return ErrorResponse.builder()
                .error(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.name())
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseErrorResponse baseErrorResponse(MethodArgumentNotValidException exception){
        var errors = new ArrayList<String>();
        exception.getAllErrors()
                .forEach( error -> errors.add(error.getDefaultMessage()));
        return ErrosResponse.builder()
                .errors(errors)
                .status(HttpStatus.BAD_REQUEST.name())
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .build();
    }
}
