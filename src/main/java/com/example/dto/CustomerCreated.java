package com.example.dto;

public record CustomerCreated(
        Long idCustomer,
        String name,
        String email,
        String phone,
        String address
) {}
