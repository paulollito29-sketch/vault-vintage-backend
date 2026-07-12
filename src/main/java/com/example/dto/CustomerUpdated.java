package com.example.dto;

public record CustomerUpdated(
        Long idCustomer,
        String name,
        String email,
        String phone,
        String address
) {}
