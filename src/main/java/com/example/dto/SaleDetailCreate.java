package com.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SaleDetailCreate(
        @NotNull @Min(1) Integer quantity,
        @NotNull Long productId,
        @NotNull Long saleId
) {}
