package com.example.service;

import com.example.dto.*;
import com.example.entity.CategoryEntity;
import com.example.entity.ProductEntity;
import com.example.exception.ResourceAlreadyExistsException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.ProductMapper;
import com.example.repository.CategoryRepository;
import com.example.repository.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;

    public List<ProductFindAll> findAll() {
        return repository.findAllByEnabledIsTrueOrderByIdProductDesc().stream()
                .map(ProductMapper::toFindAll)
                .toList();
    }

    public List<ProductFindAll> findByCategories(List<Long> categoryIds) {
        return repository.findAllByEnabledIsTrueAndCategory_IdCategoryIn(categoryIds).stream()
                .map(ProductMapper::toFindAll)
                .toList();
    }

    public ProductCreated create(ProductCreate dto) {
        if (repository.existsByEnabledIsTrueAndNameIgnoreCase(dto.name())) {
            throw new ResourceAlreadyExistsException("Product '" + dto.name() + "' already exists");
        }
        var category = findCategory(dto.categoryId());
        var entity = ProductMapper.toEntity(dto, category);
        return ProductMapper.toCreated(repository.save(entity));
    }

    public ProductFindOne findOne(Long id) {
        var entity = repository.findFirstByEnabledIsTrueAndIdProduct(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return ProductMapper.toFindOne(entity);
    }

    public ProductUpdated update(Long id, ProductUpdate dto) {
        var entity = repository.findFirstByEnabledIsTrueAndIdProduct(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        if (repository.existsByEnabledIsTrueAndNameIgnoreCaseAndIdProductNot(dto.name(), id)) {
            throw new ResourceAlreadyExistsException("Product '" + dto.name() + "' already exists");
        }
        var category = findCategory(dto.categoryId());
        return ProductMapper.toUpdated(repository.save(ProductMapper.toEntity(entity, dto, category)));
    }

    public void delete(Long id) {
        var entity = repository.findFirstByEnabledIsTrueAndIdProduct(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        repository.save(ProductMapper.toEntity(entity));
    }

    public List<ProductFindAll> search(String q, List<Long> categoryIds, Integer minCondition, Integer maxCondition, Boolean available, String sex, String size) {
        Specification<ProductEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isTrue(root.get("enabled")));

            if (q != null && !q.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + q.toLowerCase() + "%"));
            }
            if (categoryIds != null && !categoryIds.isEmpty()) {
                predicates.add(root.get("category").get("idCategory").in(categoryIds));
            }
            if (minCondition != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("condition"), minCondition));
            }
            if (maxCondition != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("condition"), maxCondition));
            }
            if (available != null) {
                predicates.add(cb.equal(root.get("available"), available));
            }
            if (sex != null && !sex.isBlank()) {
                predicates.add(cb.equal(root.get("sex"), sex));
            }
            if (size != null && !size.isBlank()) {
                predicates.add(cb.equal(root.get("size"), size));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return repository.findAll(spec).stream()
                .map(ProductMapper::toFindAll)
                .toList();
    }

    private CategoryEntity findCategory(Long categoryId) {
        return categoryRepository.findByEnabledIsTrueAndIdCategory(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
    }
}
