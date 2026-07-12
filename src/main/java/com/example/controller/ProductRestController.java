package com.example.controller;

import com.example.dto.*;
import com.example.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService service;

    @GetMapping
    public ResponseEntity<List<ProductFindAll>> findAll(
            @RequestParam(required = false) List<Long> category) {
        if (category != null && !category.isEmpty()) {
            return ResponseEntity.ok(service.findByCategories(category));
        }
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductFindAll>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) List<Long> category,
            @RequestParam(required = false) Integer minCondition,
            @RequestParam(required = false) Integer maxCondition,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) String sex,
            @RequestParam(required = false) String size) {
        return ResponseEntity.ok(service.search(q, category, minCondition, maxCondition, available, sex, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductFindOne> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.findOne(id));
    }

    @PostMapping
    public ResponseEntity<ProductCreated> create(@Valid @RequestBody ProductCreate dto) {
        var created = service.create(dto);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.idProduct()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductUpdated> update(@PathVariable Long id,
                                                  @Valid @RequestBody ProductUpdate dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
