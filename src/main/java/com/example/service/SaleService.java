package com.example.service;

import com.example.dto.*;
import com.example.entity.CustomerEntity;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.SaleMapper;
import com.example.repository.CustomerRepository;
import com.example.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository repository;
    private final CustomerRepository customerRepository;

    public List<SaleFindAll> findAll() {
        return repository.findAllByEnabledIsTrueOrderByIdSaleDesc().stream()
                .map(SaleMapper::toFindAll)
                .toList();
    }

    public SaleCreated create(SaleCreate dto) {
        var customer = findCustomer(dto.customerId());
        var entity = SaleMapper.toEntity(dto, customer);
        return SaleMapper.toCreated(repository.save(entity));
    }

    public SaleFindOne findOne(Long id) {
        var entity = repository.findFirstByEnabledIsTrueAndIdSale(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));
        return SaleMapper.toFindOne(entity);
    }

    public SaleUpdated update(Long id, SaleUpdate dto) {
        var entity = repository.findFirstByEnabledIsTrueAndIdSale(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));
        var customer = findCustomer(dto.customerId());
        return SaleMapper.toUpdated(repository.save(SaleMapper.toEntity(entity, dto, customer)));
    }

    public void delete(Long id) {
        var entity = repository.findFirstByEnabledIsTrueAndIdSale(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));
        repository.save(SaleMapper.toEntity(entity));
    }

    private CustomerEntity findCustomer(Long customerId) {
        return customerRepository.findFirstByEnabledIsTrueAndIdCustomer(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
    }
}
