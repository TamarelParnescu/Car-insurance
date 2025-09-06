package com.example.carins.web;

import com.example.carins.exception.DateOutOfRangeException;
import com.example.carins.exception.InvalidInsuranceException;
import com.example.carins.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationErrors(MethodArgumentNotValidException ex)
    {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return errors;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleTypeMismatch(MethodArgumentTypeMismatchException ex)
    {
        return Map.of("error", "RequestedParam/PathVariable is not valid");
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMissingServletRequestParameter(MissingServletRequestParameterException ex)
    {
        return Map.of("error", "Requested Param is required");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleEmptyBody(HttpMessageNotReadableException ex) {
        return Map.of("error", "RequestBody is required");
    }

    @ExceptionHandler(InvalidInsuranceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidInsurance(InvalidInsuranceException ex)
    {
        return Map.of("error", ex.getMessage());
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleResourceNotFound(ResourceNotFoundException ex)
    {
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(DateOutOfRangeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handleDateOutOfRange(DateOutOfRangeException ex)
    {
        return Map.of("error", ex.getMessage());
    }


}