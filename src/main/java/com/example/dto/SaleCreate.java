package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SaleCreate(
        String description,
        @NotNull Long customerId
) {}
