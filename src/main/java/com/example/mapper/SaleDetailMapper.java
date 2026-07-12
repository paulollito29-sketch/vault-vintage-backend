package com.example.mapper;

import com.example.dto.*;
import com.example.entity.ProductEntity;
import com.example.entity.SaleDetailEntity;
import com.example.entity.SaleEntity;

import java.time.LocalDateTime;

public class SaleDetailMapper {

    private SaleDetailMapper() {}

    public static SaleDetailEntity toEntity(SaleDetailCreate dto, ProductEntity product, SaleEntity sale) {
        double unitPrice = product.getPrice();
        double total = unitPrice * dto.quantity();
        return SaleDetailEntity.builder()
                .quantity(dto.quantity())
                .unitPrice(unitPrice)
                .total(total)
                .product(product)
                .sale(sale)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static SaleDetailEntity toEntity(SaleDetailEntity entity, SaleDetailUpdate dto,
                                             ProductEntity product, SaleEntity sale) {
        double unitPrice = product.getPrice();
        double total = unitPrice * dto.quantity();
        entity.setQuantity(dto.quantity());
        entity.setUnitPrice(unitPrice);
        entity.setTotal(total);
        entity.setProduct(product);
        entity.setSale(sale);
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    public static SaleDetailEntity toEntity(SaleDetailEntity entity) {
        entity.setEnabled(false);
        entity.setDeletedAt(LocalDateTime.now());
        return entity;
    }

    public static SaleDetailCreated toCreated(SaleDetailEntity entity) {
        return new SaleDetailCreated(entity.getIdSaleDetail(), entity.getQuantity(),
                entity.getUnitPrice(), entity.getTotal(),
                entity.getProduct().getIdProduct(), entity.getSale().getIdSale());
    }

    public static SaleDetailFindAll toFindAll(SaleDetailEntity entity) {
        return new SaleDetailFindAll(entity.getIdSaleDetail(), entity.getQuantity(),
                entity.getUnitPrice(), entity.getTotal(),
                entity.getProduct().getIdProduct(), entity.getProduct().getName(),
                entity.getSale().getIdSale());
    }

    public static SaleDetailFindOne toFindOne(SaleDetailEntity entity) {
        return new SaleDetailFindOne(entity.getIdSaleDetail(), entity.getQuantity(),
                entity.getUnitPrice(), entity.getTotal(),
                entity.getProduct().getIdProduct(), entity.getProduct().getName(),
                entity.getSale().getIdSale());
    }

    public static SaleDetailUpdated toUpdated(SaleDetailEntity entity) {
        return new SaleDetailUpdated(entity.getIdSaleDetail(), entity.getQuantity(),
                entity.getUnitPrice(), entity.getTotal(),
                entity.getProduct().getIdProduct(), entity.getSale().getIdSale());
    }
}
