package com.example.mapper;

import com.example.dto.*;
import com.example.entity.CategoryEntity;
import com.example.entity.ProductEntity;

import java.time.LocalDateTime;

public class ProductMapper {

    private ProductMapper() {}

    public static ProductEntity toEntity(ProductCreate dto, CategoryEntity category) {
        return ProductEntity.builder()
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .size(dto.size())
                .condition(dto.condition())
                .imageUrl(dto.imageUrl())
                .available(dto.available())
                .sex(dto.sex())
                .category(category)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ProductEntity toEntity(ProductEntity entity, ProductUpdate dto, CategoryEntity category) {
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        entity.setSize(dto.size());
        entity.setCondition(dto.condition());
        entity.setImageUrl(dto.imageUrl());
        entity.setAvailable(dto.available());
        entity.setSex(dto.sex());
        entity.setCategory(category);
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    public static ProductEntity toEntity(ProductEntity entity) {
        entity.setEnabled(false);
        entity.setDeletedAt(LocalDateTime.now());
        return entity;
    }

    public static ProductCreated toCreated(ProductEntity entity) {
        return new ProductCreated(
                entity.getIdProduct(), entity.getName(), entity.getDescription(),
                entity.getPrice(), entity.getSize(), entity.getCondition(),
                entity.getImageUrl(), entity.getAvailable(), entity.getSex(),
                entity.getCategory().getIdCategory(),
                entity.getCategory().getName());
    }

    public static ProductFindAll toFindAll(ProductEntity entity) {
        return new ProductFindAll(
                entity.getIdProduct(), entity.getName(), entity.getPrice(),
                entity.getSize(), entity.getCondition(), entity.getImageUrl(),
                entity.getAvailable(), entity.getSex(),
                entity.getCategory().getIdCategory(), entity.getCategory().getName());
    }

    public static ProductFindOne toFindOne(ProductEntity entity) {
        return new ProductFindOne(
                entity.getIdProduct(), entity.getName(), entity.getDescription(),
                entity.getPrice(), entity.getSize(), entity.getCondition(),
                entity.getImageUrl(), entity.getAvailable(), entity.getSex(),
                entity.getCategory().getIdCategory(),
                entity.getCategory().getName());
    }

    public static ProductUpdated toUpdated(ProductEntity entity) {
        return new ProductUpdated(
                entity.getIdProduct(), entity.getName(), entity.getDescription(),
                entity.getPrice(), entity.getSize(), entity.getCondition(),
                entity.getImageUrl(), entity.getAvailable(), entity.getSex(),
                entity.getCategory().getIdCategory(),
                entity.getCategory().getName());
    }
}
