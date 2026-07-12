package com.example.mapper;

import com.example.dto.*;
import com.example.entity.CustomerEntity;

import java.time.LocalDateTime;

public class CustomerMapper {

    private CustomerMapper() {}

    public static CustomerEntity toEntity(CustomerCreate dto) {
        return CustomerEntity.builder()
                .name(dto.name())
                .email(dto.email())
                .phone(dto.phone())
                .address(dto.address())
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static CustomerEntity toEntity(CustomerEntity entity, CustomerUpdate dto) {
        entity.setName(dto.name());
        entity.setEmail(dto.email());
        entity.setPhone(dto.phone());
        entity.setAddress(dto.address());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }

    public static CustomerEntity toEntity(CustomerEntity entity) {
        entity.setEnabled(false);
        entity.setDeletedAt(LocalDateTime.now());
        return entity;
    }

    public static CustomerCreated toCreated(CustomerEntity entity) {
        return new CustomerCreated(
                entity.getIdCustomer(), entity.getName(), entity.getEmail(),
                entity.getPhone(), entity.getAddress());
    }

    public static CustomerFindAll toFindAll(CustomerEntity entity) {
        return new CustomerFindAll(
                entity.getIdCustomer(), entity.getName(), entity.getEmail(),
                entity.getPhone(), entity.getAddress());
    }

    public static CustomerFindOne toFindOne(CustomerEntity entity) {
        return new CustomerFindOne(
                entity.getIdCustomer(), entity.getName(), entity.getEmail(),
                entity.getPhone(), entity.getAddress());
    }

    public static CustomerUpdated toUpdated(CustomerEntity entity) {
        return new CustomerUpdated(
                entity.getIdCustomer(), entity.getName(), entity.getEmail(),
                entity.getPhone(), entity.getAddress());
    }
}
