package com.budget.backend.domain.model;

import com.budget.backend.domain.exception.InvalidCategoryException;
import com.budget.backend.domain.enums.CategoryType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Category {

    private final UUID id;
    private final String name;
    private final CategoryType type;
    private final String description;
    private final String icon;
    private final LocalDateTime createdAt;

    public static Category create(String name, CategoryType type, String description, String icon) {
        Category category = Category.builder()
                .id(UUID.randomUUID())
                .name(name)
                .type(type)
                .description(description)
                .icon(icon)
                .createdAt(LocalDateTime.now())
                .build();

        category.validate();
        return category;
    }

    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidCategoryException("Le nom de la catégorie est obligatoire");
        }

        if (name.length() > 100) {
            throw new InvalidCategoryException("Le nom de la catégorie ne doit pas dépasser 100 caractères");
        }

        if (type == null) {
            throw new InvalidCategoryException("Le type de catégorie est obligatoire");
        }
    }
}