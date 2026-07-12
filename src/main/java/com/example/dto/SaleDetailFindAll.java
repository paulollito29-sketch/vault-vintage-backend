package com.example.dto;

public record SaleDetailFindAll(
        Long idSaleDetail,
        Integer quantity,
        Double unitPrice,
        Double total,
        Long productId,
        String productName,
        Long saleId
) {}
