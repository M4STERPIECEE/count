package com.budget.backend.infrastructure.persistence.adapter;

import com.budget.backend.application.port.output.CategoryRepositoryPort;
import com.budget.backend.domain.model.Category;
import com.budget.backend.infrastructure.persistence.entity.CategoryEntity;
import com.budget.backend.infrastructure.persistence.repository.JpaCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional
public class CategoryRepositoryAdapter implements CategoryRepositoryPort {

    private final JpaCategoryRepository jpaCategoryRepository;

    @Override
    public Category save(Category category) {
        CategoryEntity entity = mapToEntity(category);
        CategoryEntity savedEntity = jpaCategoryRepository.save(entity);
        return mapToDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findById(UUID id) {
        return jpaCategoryRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return jpaCategoryRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaCategoryRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaCategoryRepository.existsById(id);
    }

    private CategoryEntity mapToEntity(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryEntity.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .description(category.getDescription())
                .icon(category.getIcon())
                .createdAt(category.getCreatedAt())
                .build();
    }

    private Category mapToDomain(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        return Category.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType())
                .description(entity.getDescription())
                .icon(entity.getIcon())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}