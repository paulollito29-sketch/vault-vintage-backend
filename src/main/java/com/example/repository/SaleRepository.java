package com.example.repository;

import com.example.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<SaleEntity, Long> {
    List<SaleEntity> findAllByEnabledIsTrueOrderByIdSaleDesc();
    Optional<SaleEntity> findFirstByEnabledIsTrueAndIdSale(Long id);
    List<SaleEntity> findAllByEnabledIsTrueAndCustomer_IdCustomer(Long customerId);
}
