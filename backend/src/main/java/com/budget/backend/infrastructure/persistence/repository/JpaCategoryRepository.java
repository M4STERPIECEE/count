package com.budget.backend.infrastructure.persistence.repository;

import com.budget.backend.infrastructure.persistence.entity.CategoryEntity;
import com.budget.backend.domain.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaCategoryRepository extends JpaRepository<CategoryEntity, UUID> {

    Optional<CategoryEntity> findByName(String name);

    List<CategoryEntity> findByType(CategoryType type);

    boolean existsByName(String name);
}
