package com.example.dto;

import java.time.LocalDate;

public record SaleFindOne(
        Long idSale,
        Double subTotal,
        Double tax,
        Double total,
        String description,
        Long customerId,
        String customerName,
        LocalDate saleDate
) {}
