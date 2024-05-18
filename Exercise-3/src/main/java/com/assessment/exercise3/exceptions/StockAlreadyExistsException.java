package com.assessment.exercise3.exceptions;

public class StockAlreadyExistsException extends RuntimeException {
    public StockAlreadyExistsException(String message) {
        super(message);
    }
}
