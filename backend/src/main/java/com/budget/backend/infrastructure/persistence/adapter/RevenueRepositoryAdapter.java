package com.budget.backend.infrastructure.persistence.adapter;

import com.budget.backend.application.port.output.RevenueRepositoryPort;
import com.budget.backend.domain.model.Category;
import com.budget.backend.domain.model.Revenue;
import com.budget.backend.infrastructure.persistence.entity.CategoryEntity;
import com.budget.backend.infrastructure.persistence.entity.RevenueEntity;
import com.budget.backend.infrastructure.persistence.repository.JpaRevenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional
public class RevenueRepositoryAdapter implements RevenueRepositoryPort {
    
    private final JpaRevenueRepository jpaRevenueRepository;
    
    @Override
    public Revenue save(Revenue revenue) {
        RevenueEntity entity = mapToEntity(revenue);
        RevenueEntity savedEntity = jpaRevenueRepository.save(entity);
        return mapToDomain(savedEntity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Revenue> findById(UUID id) {
        return jpaRevenueRepository.findById(id).map(this::mapToDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Revenue> findAll() {
        return jpaRevenueRepository.findAll().stream().map(this::mapToDomain).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<Revenue> findAllPaginated(Pageable pageable) {
        return jpaRevenueRepository.findAllByOrderByDateDesc(pageable).map(this::mapToDomain);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Revenue> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return jpaRevenueRepository.findByDateBetween(startDate, endDate).stream().map(this::mapToDomain).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<Revenue> findByYearAndMonth(int year, int month) {
        return jpaRevenueRepository.findByYearAndMonth(year, month).stream().map(this::mapToDomain).collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Revenue> findByYear(int year) {
        return jpaRevenueRepository.findByYear(year).stream().map(this::mapToDomain).collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(UUID id) {
        jpaRevenueRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(UUID id) {
        return jpaRevenueRepository.existsById(id);
    }
    
    private RevenueEntity mapToEntity(Revenue revenue) {
        if (revenue == null) {
            return null;
        }
        
        CategoryEntity categoryEntity = CategoryEntity.builder()
                .id(revenue.getCategory().getId())
                .name(revenue.getCategory().getName())
                .type(revenue.getCategory().getType())
                .description(revenue.getCategory().getDescription())
                .icon(revenue.getCategory().getIcon())
                .build();
        
        return RevenueEntity.builder()
                .id(revenue.getId())
                .amount(revenue.getAmount())
                .source(revenue.getSource())
                .date(revenue.getDate())
                .category(categoryEntity)
                .description(revenue.getDescription())
                .type(revenue.getType())
                .createdAt(revenue.getCreatedAt())
                .updatedAt(revenue.getUpdatedAt())
                .build();
    }
    
    private Revenue mapToDomain(RevenueEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Category category = Category.builder()
                .id(entity.getCategory().getId())
                .name(entity.getCategory().getName())
                .type(entity.getCategory().getType())
                .description(entity.getCategory().getDescription())
                .icon(entity.getCategory().getIcon())
                .createdAt(entity.getCategory().getCreatedAt())
                .build();
        
        return Revenue.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .source(entity.getSource())
                .date(entity.getDate())
                .category(category)
                .description(entity.getDescription())
                .type(entity.getType())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}