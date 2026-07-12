package com.example.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryUpdate(
        @NotBlank String name,
        String description
) {}
