package com.example.repository;

import com.example.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {
    List<ProductEntity> findAllByEnabledIsTrueOrderByIdProductDesc();
    Optional<ProductEntity> findFirstByEnabledIsTrueAndIdProduct(Long id);
    boolean existsByEnabledIsTrueAndNameIgnoreCase(String name);
    boolean existsByEnabledIsTrueAndNameIgnoreCaseAndIdProductNot(String name, Long id);
    List<ProductEntity> findAllByEnabledIsTrueAndCategory_IdCategoryIn(List<Long> categoryIds);
}
