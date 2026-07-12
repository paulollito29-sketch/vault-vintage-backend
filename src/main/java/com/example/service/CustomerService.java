package com.example.service;

import com.example.dto.*;
import com.example.exception.ResourceAlreadyExistsException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.CustomerMapper;
import com.example.repository.CustomerRepository;
import com.example.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final SaleRepository saleRepository;

    public List<CustomerFindAll> findAll() {
        return repository.findAllByEnabledIsTrueOrderByIdCustomerDesc().stream()
                .map(CustomerMapper::toFindAll)
                .toList();
    }

    public CustomerCreated create(CustomerCreate dto) {
        if (repository.existsByEnabledIsTrueAndEmailIgnoreCase(dto.email())) {
            throw new ResourceAlreadyExistsException("Email '" + dto.email() + "' already registered");
        }
        var entity = CustomerMapper.toEntity(dto);
        return CustomerMapper.toCreated(repository.save(entity));
    }

    public CustomerFindOne findOne(Long id) {
        var entity = repository.findFirstByEnabledIsTrueAndIdCustomer(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return CustomerMapper.toFindOne(entity);
    }

    public CustomerUpdated update(Long id, CustomerUpdate dto) {
        var entity = repository.findFirstByEnabledIsTrueAndIdCustomer(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        if (repository.existsByEnabledIsTrueAndEmailIgnoreCaseAndIdCustomerNot(dto.email(), id)) {
            throw new ResourceAlreadyExistsException("Email '" + dto.email() + "' already registered");
        }
        return CustomerMapper.toUpdated(repository.save(CustomerMapper.toEntity(entity, dto)));
    }

    public void delete(Long id) {
        var entity = repository.findFirstByEnabledIsTrueAndIdCustomer(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        repository.save(CustomerMapper.toEntity(entity));
    }
}
