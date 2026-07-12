package com.example.dto;

import java.util.List;

public record CategoryFindOne(
        Long idCategory,
        String name,
        String description,
        List<ProductFindAll> products
) {}
