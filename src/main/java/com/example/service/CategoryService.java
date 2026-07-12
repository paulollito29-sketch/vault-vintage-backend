package com.example.service;

import com.example.dto.*;
import com.example.entity.CategoryEntity;
import com.example.exception.ResourceAlreadyExistsException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.CategoryMapper;
import com.example.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public List<CategoryFindAll> findAll() {
        return repository.findAllByEnabledIsTrueOrderByIdCategoryDesc().stream()
                .map(CategoryMapper::toFindAll)
                .toList();
    }

    public CategoryCreated create(CategoryCreate dto) {
        if (repository.existsByEnabledIsTrueAndNameIgnoreCase(dto.name())) {
            throw new ResourceAlreadyExistsException("Category '" + dto.name() + "' already exists");
        }
        var entity = CategoryMapper.toEntity(dto);
        return CategoryMapper.toCreated(repository.save(entity));
    }

    public CategoryFindOne findOne(Long id) {
        var entity = repository.findByEnabledIsTrueAndIdCategory(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return CategoryMapper.toFindOne(entity);
    }

    public CategoryUpdated update(Long id, CategoryUpdate dto) {
        var entity = repository.findByEnabledIsTrueAndIdCategory(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        if (repository.existsByEnabledIsTrueAndIdCategoryNotAndNameIgnoreCase(id, dto.name())) {
            throw new ResourceAlreadyExistsException("Category '" + dto.name() + "' already exists");
        }
        return CategoryMapper.toUpdated(repository.save(CategoryMapper.toEntity(entity, dto)));
    }

    public void delete(Long id) {
        var entity = repository.findByEnabledIsTrueAndIdCategory(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        repository.save(CategoryMapper.toEntity(entity));
    }
}
