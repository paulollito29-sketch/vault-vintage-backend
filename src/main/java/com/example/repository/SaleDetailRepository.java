package com.example.repository;

import com.example.entity.SaleDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SaleDetailRepository extends JpaRepository<SaleDetailEntity, Long> {
    List<SaleDetailEntity> findAllBySale_IdSaleAndEnabledIsTrueOrderByIdSaleDetailDesc(Long saleId);
    Optional<SaleDetailEntity> findFirstByEnabledIsTrueAndIdSaleDetail(Long id);
    boolean existsByEnabledIsTrueAndSale_IdSaleAndProduct_IdProduct(Long saleId, Long productId);
    boolean existsByEnabledIsTrueAndSale_IdSaleAndProduct_IdProductAndIdSaleDetailNot(
            Long saleId, Long productId, Long id);
}
