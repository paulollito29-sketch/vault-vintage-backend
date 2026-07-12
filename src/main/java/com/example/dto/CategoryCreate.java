package com.example.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreate(
        @NotBlank String name,
        String description
) {}
