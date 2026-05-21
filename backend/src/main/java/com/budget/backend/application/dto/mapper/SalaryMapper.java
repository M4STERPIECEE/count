package com.budget.backend.application.dto.mapper;

import com.budget.backend.application.dto.SalaryDTO;
import com.budget.backend.domain.model.Salary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = {UUID.class, LocalDateTime.class})
public interface SalaryMapper {

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    Salary toDomain(SalaryDTO dto);

    SalaryDTO toDTO(Salary salary);
}