package com.assessment.exercise3.exceptions;

public class StocksNotAvailableException extends RuntimeException {
    public StocksNotAvailableException(String message) {
        super(message);
    }
}
