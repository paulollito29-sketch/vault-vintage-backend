package com.example.controller;

import com.example.dto.*;
import com.example.service.SaleDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/sale-details")
@RequiredArgsConstructor
public class SaleDetailRestController {

    private final SaleDetailService service;

    @GetMapping
    public ResponseEntity<List<SaleDetailFindAll>> findAll(@RequestParam Long saleId) {
        return ResponseEntity.ok(service.findAll(saleId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDetailFindOne> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.findOne(id));
    }

    @PostMapping
    public ResponseEntity<SaleDetailCreated> create(@Valid @RequestBody SaleDetailCreate dto) {
        var created = service.create(dto);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.idSaleDetail()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleDetailUpdated> update(@PathVariable Long id,
                                                     @Valid @RequestBody SaleDetailUpdate dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
