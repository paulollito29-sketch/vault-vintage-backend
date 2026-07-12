package com.example.mapper;

import com.example.dto.*;
import com.example.entity.CategoryEntity;
import com.example.entity.ProductEntity;

import java.time.LocalDateTime;
import java.util.List;

public class CategoryMapper {

    private CategoryMapper() {}

    public static CategoryEntity toEntity(CategoryCreate dto) {
        return CategoryEntity.builder()
                .name(dto.name())
                .description(dto.description())
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static CategoryEntity toEntity(CategoryEntity entity, CategoryUpdate dto) {
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    public static CategoryEntity toEntity(CategoryEntity entity) {
        entity.setEnabled(false);
        entity.setDeletedAt(LocalDateTime.now());
        return entity;
    }

    public static CategoryCreated toCreated(CategoryEntity entity) {
        return new CategoryCreated(entity.getIdCategory(), entity.getName(), entity.getDescription());
    }

    public static CategoryFindAll toFindAll(CategoryEntity entity) {
        return new CategoryFindAll(
                entity.getIdCategory(),
                entity.getName(),
                entity.getDescription(),
                entity.getProducts().stream().filter(ProductEntity::getEnabled).count()
        );
    }

    public static CategoryFindOne toFindOne(CategoryEntity entity) {
        return new CategoryFindOne(
                entity.getIdCategory(),
                entity.getName(),
                entity.getDescription(),
                entity.getProducts().stream()
                        .filter(ProductEntity::getEnabled)
                        .map(ProductMapper::toFindAll)
                        .toList()
        );
    }

    public static CategoryUpdated toUpdated(CategoryEntity entity) {
        return new CategoryUpdated(entity.getIdCategory(), entity.getName(), entity.getDescription());
    }

    public static List<ProductFindAll> toProducts(List<ProductEntity> products) {
        return products.stream().filter(ProductEntity::getEnabled).map(ProductMapper::toFindAll).toList();
    }
}
