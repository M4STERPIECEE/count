package com.budget.backend.application.port.output;

import com.budget.backend.domain.model.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepositoryPort {

    Category save(Category category);

    Optional<Category> findById(UUID id);

    List<Category> findAll();

    void deleteById(UUID id);

    boolean existsById(UUID id);
}