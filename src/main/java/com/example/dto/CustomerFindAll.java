package com.example.dto;

public record CustomerFindAll(
        Long idCustomer,
        String name,
        String email,
        String phone,
        String address
) {}
