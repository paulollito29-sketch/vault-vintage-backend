package com.example.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductUpdate(
        @NotBlank String name,
        String description,
        @NotNull @Min(0) Double price,
        String size,
        @NotNull @Min(1) @Max(5) Integer condition,
        String imageUrl,
        @NotNull Boolean available,
        String sex,
        @NotNull Long categoryId
) {}
