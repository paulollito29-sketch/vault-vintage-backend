package com.example.mapper;

import com.example.dto.*;
import com.example.entity.CustomerEntity;
import com.example.entity.SaleEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SaleMapper {

    private SaleMapper() {}

    public static SaleEntity toEntity(SaleCreate dto, CustomerEntity customer) {
        return SaleEntity.builder()
                .subTotal(0.0)
                .tax(0.0)
                .total(0.0)
                .saleDate(LocalDate.now())
                .description(dto.description())
                .customer(customer)
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static SaleEntity toEntity(SaleEntity entity, SaleUpdate dto, CustomerEntity customer) {
        entity.setDescription(dto.description());
        entity.setCustomer(customer);
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    public static SaleEntity toEntity(SaleEntity entity) {
        entity.setEnabled(false);
        entity.setDeletedAt(LocalDateTime.now());
        return entity;
    }

    public static SaleCreated toCreated(SaleEntity entity) {
        return new SaleCreated(entity.getIdSale(), entity.getSubTotal(),
                entity.getTax(), entity.getTotal(), entity.getDescription(),
                entity.getCustomer().getIdCustomer(), entity.getSaleDate());
    }

    public static SaleFindAll toFindAll(SaleEntity entity) {
        return new SaleFindAll(entity.getIdSale(), entity.getSubTotal(),
                entity.getTax(), entity.getTotal(), entity.getDescription(),
                entity.getCustomer().getIdCustomer(), entity.getCustomer().getName(),
                entity.getSaleDate());
    }

    public static SaleFindOne toFindOne(SaleEntity entity) {
        return new SaleFindOne(entity.getIdSale(), entity.getSubTotal(),
                entity.getTax(), entity.getTotal(), entity.getDescription(),
                entity.getCustomer().getIdCustomer(), entity.getCustomer().getName(),
                entity.getSaleDate());
    }

    public static SaleUpdated toUpdated(SaleEntity entity) {
        return new SaleUpdated(entity.getIdSale(), entity.getSubTotal(),
                entity.getTax(), entity.getTotal(), entity.getDescription(),
                entity.getCustomer().getIdCustomer(), entity.getSaleDate());
    }
}
