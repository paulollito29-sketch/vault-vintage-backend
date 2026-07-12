package com.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SaleDetailUpdate(
        @NotNull @Min(1) Integer quantity,
        @NotNull Long productId,
        @NotNull Long saleId
) {}
