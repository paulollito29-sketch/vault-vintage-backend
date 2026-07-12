package com.example.dto;

public record ProductCreated(
        Long idProduct,
        String name,
        String description,
        Double price,
        String size,
        Integer condition,
        String imageUrl,
        Boolean available,
        String sex,
        Long categoryId,
        String categoryName
) {}
