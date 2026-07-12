package com.example.dto;

public record SaleDetailCreated(
        Long idSaleDetail,
        Integer quantity,
        Double unitPrice,
        Double total,
        Long productId,
        Long saleId
) {}
