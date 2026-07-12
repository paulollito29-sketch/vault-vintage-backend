package com.example.dto;

public record ProductFindAll(
        Long idProduct,
        String name,
        Double price,
        String size,
        Integer condition,
        String imageUrl,
        Boolean available,
        String sex,
        Long categoryId,
        String categoryName
) {}
