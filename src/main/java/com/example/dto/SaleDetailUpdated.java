package com.example.dto;

public record SaleDetailUpdated(
        Long idSaleDetail,
        Integer quantity,
        Double unitPrice,
        Double total,
        Long productId,
        Long saleId
) {}
