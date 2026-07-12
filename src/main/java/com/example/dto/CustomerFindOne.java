package com.example.dto;

public record CustomerFindOne(
        Long idCustomer,
        String name,
        String email,
        String phone,
        String address
) {}
