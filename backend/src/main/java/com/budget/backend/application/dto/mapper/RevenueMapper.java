package com.budget.backend.application.dto.mapper;

import com.budget.backend.application.dto.RevenueDTO;
import com.budget.backend.domain.model.Category;
import com.budget.backend.domain.model.Revenue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = {UUID.class, LocalDateTime.class})
public interface RevenueMapper {

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "category", source = "categoryId", qualifiedByName = "categoryFromId")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    Revenue toDomain(RevenueDTO dto);

    @Mapping(target = "categoryId", source = "category.id")
    RevenueDTO toDTO(Revenue revenue);

    @Named("categoryFromId")
    default Category categoryFromId(UUID categoryId) {
        if (categoryId == null) {
            return null;
        }
        return Category.builder()
                .id(categoryId)
                .build();
    }
}
