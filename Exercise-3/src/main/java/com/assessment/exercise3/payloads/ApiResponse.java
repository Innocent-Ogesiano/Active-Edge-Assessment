package com.assessment.exercise3.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;

    public ApiResponse (int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
