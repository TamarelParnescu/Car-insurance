package com.example.carins.exception;

public class InvalidInsuranceException extends RuntimeException{
    public InvalidInsuranceException(String message) {
        super(message);
    }
}