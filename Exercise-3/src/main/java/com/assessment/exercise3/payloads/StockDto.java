package com.assessment.exercise3.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StockDto(
        @NotBlank(message = "Stock name is required")
        String name,
        @NotNull(message = "Current price cannot be null")
        Double currentPrice
) {
}
