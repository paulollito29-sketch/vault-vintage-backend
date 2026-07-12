package com.example.service;

import com.example.dto.*;
import com.example.entity.ProductEntity;
import com.example.entity.SaleDetailEntity;
import com.example.entity.SaleEntity;
import com.example.exception.ResourceAlreadyExistsException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.SaleDetailMapper;
import com.example.repository.ProductRepository;
import com.example.repository.SaleDetailRepository;
import com.example.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleDetailService {

    private static final double TAX_RATE = 0.18;

    private final SaleDetailRepository repository;
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    public List<SaleDetailFindAll> findAll(Long saleId) {
        return repository.findAllBySale_IdSaleAndEnabledIsTrueOrderByIdSaleDetailDesc(saleId).stream()
                .map(SaleDetailMapper::toFindAll)
                .toList();
    }

    public SaleDetailCreated create(SaleDetailCreate dto) {
        var product = findProduct(dto.productId());
        var sale = findSale(dto.saleId());
        if (repository.existsByEnabledIsTrueAndSale_IdSaleAndProduct_IdProduct(dto.saleId(), dto.productId())) {
            throw new ResourceAlreadyExistsException("Product already in this sale");
        }
        var entity = SaleDetailMapper.toEntity(dto, product, sale);
        var saved = repository.save(entity);
        recalcSaleTotals(sale);
        return SaleDetailMapper.toCreated(saved);
    }

    public SaleDetailFindOne findOne(Long id) {
        var entity = repository.findFirstByEnabledIsTrueAndIdSaleDetail(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale detail not found with id: " + id));
        return SaleDetailMapper.toFindOne(entity);
    }

    public SaleDetailUpdated update(Long id, SaleDetailUpdate dto) {
        var entity = repository.findFirstByEnabledIsTrueAndIdSaleDetail(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale detail not found with id: " + id));
        var product = findProduct(dto.productId());
        var sale = findSale(dto.saleId());
        if (repository.existsByEnabledIsTrueAndSale_IdSaleAndProduct_IdProductAndIdSaleDetailNot(
                dto.saleId(), dto.productId(), id)) {
            throw new ResourceAlreadyExistsException("Product already in this sale");
        }
        var updated = SaleDetailMapper.toEntity(entity, dto, product, sale);
        var saved = repository.save(updated);
        recalcSaleTotals(sale);
        return SaleDetailMapper.toUpdated(saved);
    }

    public void delete(Long id) {
        var entity = repository.findFirstByEnabledIsTrueAndIdSaleDetail(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale detail not found with id: " + id));
        var sale = entity.getSale();
        repository.save(SaleDetailMapper.toEntity(entity));
        recalcSaleTotals(sale);
    }

    private void recalcSaleTotals(SaleEntity sale) {
        var details = repository.findAllBySale_IdSaleAndEnabledIsTrueOrderByIdSaleDetailDesc(sale.getIdSale());
        double subTotal = details.stream().mapToDouble(SaleDetailEntity::getTotal).sum();
        sale.setSubTotal(subTotal);
        sale.setTax(subTotal * TAX_RATE);
        sale.setTotal(subTotal + sale.getTax());
        sale.setUpdatedAt(java.time.LocalDateTime.now());
        saleRepository.save(sale);
    }

    private ProductEntity findProduct(Long id) {
        return productRepository.findFirstByEnabledIsTrueAndIdProduct(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    private SaleEntity findSale(Long id) {
        return saleRepository.findFirstByEnabledIsTrueAndIdSale(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));
    }
}
