package com.example.dto;

import jakarta.validation.constraints.NotNull;

public record SaleUpdate(
        String description,
        @NotNull Long customerId
) {}
