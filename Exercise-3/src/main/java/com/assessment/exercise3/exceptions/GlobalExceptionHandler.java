package com.assessment.exercise3.exceptions;

import com.assessment.exercise3.payloads.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(StockAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleStockAlreadyExistsException (StockAlreadyExistsException e) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleStockNotFoundException(StockNotFoundException e) {
        return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(StocksNotAvailableException.class)
    public ResponseEntity<ApiResponse<String>> handleStocksNotAvailableException(StocksNotAvailableException e) {
        return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    private ResponseEntity<ApiResponse<String>> createErrorResponse(HttpStatus httpStatus, String message) {
        ApiResponse<String> response = new ApiResponse<>(httpStatus.value(), "Failed", message);
        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            if (errors.containsKey(error.getField()))
                errors.put(error.getField(), String.format("%s, %s", errors.get(error.getField()), errorMessage));
            else
                errors.put(error.getField(), errorMessage);
        });
        ApiResponse<Map<String, String>> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Validation error", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
