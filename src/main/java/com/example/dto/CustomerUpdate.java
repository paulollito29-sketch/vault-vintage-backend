package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerUpdate(
        @NotBlank String name,
        @NotBlank @Email String email,
        String phone,
        String address
) {}
