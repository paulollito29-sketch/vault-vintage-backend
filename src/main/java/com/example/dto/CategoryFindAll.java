package com.example.dto;

public record CategoryFindAll(
        Long idCategory,
        String name,
        String description,
        long productCount
) {}
