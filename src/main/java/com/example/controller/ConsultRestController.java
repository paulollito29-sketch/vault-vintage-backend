package com.example.controller;

import com.example.dto.SaleFindAll;
import com.example.repository.SaleRepository;
import com.example.mapper.SaleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/consult")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ConsultRestController {

    private final SaleRepository saleRepository;

    @GetMapping("/sales-between-dates")
    public List<SaleFindAll> getSalesBetweenDates(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return saleRepository.findAllByEnabledIsTrueOrderByIdSaleDesc().stream()
                .filter(s -> !s.getSaleDate().isBefore(startDate) && !s.getSaleDate().isAfter(endDate))
                .map(SaleMapper::toFindAll)
                .toList();
    }
}
