package com.budget.backend.infrastructure.persistence.adapter;

import com.budget.backend.application.port.output.SalaryRepositoryPort;
import com.budget.backend.domain.model.Salary;
import com.budget.backend.infrastructure.persistence.entity.SalaryEntity;
import com.budget.backend.infrastructure.persistence.repository.JpaSalaryRepository;
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
public class SalaryRepositoryAdapter implements SalaryRepositoryPort {

    private final JpaSalaryRepository jpaSalaryRepository;

    @Override
    public Salary save(Salary salary) {
        SalaryEntity entity = mapToEntity(salary);
        SalaryEntity savedEntity = jpaSalaryRepository.save(entity);
        return mapToDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Salary> findById(UUID id) {
        return jpaSalaryRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Salary> findAll() {
        return jpaSalaryRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Salary> findByYear(int year) {
        return jpaSalaryRepository.findByYear(year).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Salary> findByYearAndMonth(int year, int month) {
        return jpaSalaryRepository.findByYearAndMonth(year, month).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaSalaryRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaSalaryRepository.existsById(id);
    }

    private SalaryEntity mapToEntity(Salary salary) {
        if (salary == null) {
            return null;
        }

        return SalaryEntity.builder()
                .id(salary.getId())
                .amount(salary.getAmount())
                .month(salary.getMonth())
                .year(salary.getYear())
                .employer(salary.getEmployer())
                .description(salary.getDescription())
                .type(salary.getType())
                .createdAt(salary.getCreatedAt())
                .updatedAt(salary.getUpdatedAt())
                .build();
    }

    private Salary mapToDomain(SalaryEntity entity) {
        if (entity == null) {
            return null;
        }

        return Salary.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .month(entity.getMonth())
                .year(entity.getYear())
                .employer(entity.getEmployer())
                .description(entity.getDescription())
                .type(entity.getType())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}