package com.budget.backend.application.usecase;

import com.budget.backend.application.dto.SalaryDTO;
import com.budget.backend.application.dto.mapper.SalaryMapper;
import com.budget.backend.application.port.input.SalaryServicePort;
import com.budget.backend.application.port.output.SalaryRepositoryPort;
import com.budget.backend.domain.exception.DomainException;
import com.budget.backend.domain.model.Salary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ManageSalaryUseCase implements SalaryServicePort {

    private final SalaryRepositoryPort salaryRepository;
    private final SalaryMapper salaryMapper;

    @Override
    public SalaryDTO createSalary(SalaryDTO salaryDTO) {
        log.info("Création d'un nouveau salaire pour {}/{}", salaryDTO.getMonth(), salaryDTO.getYear());

        Salary salary = Salary.create(
                salaryDTO.getAmount(),
                salaryDTO.getMonth(),
                salaryDTO.getYear(),
                salaryDTO.getEmployer(),
                salaryDTO.getDescription(),
                salaryDTO.getType()
        );

        Salary savedSalary = salaryRepository.save(salary);
        log.info("Salaire créé avec succès, ID: {}", savedSalary.getId());

        return salaryMapper.toDTO(savedSalary);
    }

    @Override
    public SalaryDTO updateSalary(UUID id, SalaryDTO salaryDTO) {
        log.info("Mise à jour du salaire: {}", id);

        Salary existingSalary = salaryRepository.findById(id)
                .orElseThrow(() -> new DomainException("Salaire non trouvé avec l'ID: " + id));

        Salary updatedSalary = Salary.builder()
                .id(existingSalary.getId())
                .amount(salaryDTO.getAmount())
                .month(salaryDTO.getMonth())
                .year(salaryDTO.getYear())
                .employer(salaryDTO.getEmployer())
                .description(salaryDTO.getDescription())
                .type(salaryDTO.getType())
                .createdAt(existingSalary.getCreatedAt())
                .updatedAt(java.time.LocalDateTime.now())
                .build();

        updatedSalary.validate();
        Salary savedSalary = salaryRepository.save(updatedSalary);

        return salaryMapper.toDTO(savedSalary);
    }

    @Override
    public void deleteSalary(UUID id) {
        log.info("Suppression du salaire: {}", id);

        if (!salaryRepository.existsById(id)) {
            throw new DomainException("Salaire non trouvé avec l'ID: " + id);
        }

        salaryRepository.deleteById(id);
        log.info("Salaire supprimé avec succès: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public SalaryDTO getSalaryById(UUID id) {
        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() -> new DomainException("Salaire non trouvé avec l'ID: " + id));

        return salaryMapper.toDTO(salary);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalaryDTO> getAllSalaries() {
        return salaryRepository.findAll().stream()
                .map(salaryMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalaryDTO> getSalariesByYear(int year) {
        return salaryRepository.findByYear(year).stream()
                .map(salaryMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalaryDTO> getSalariesByMonth(int year, int month) {
        return salaryRepository.findByYearAndMonth(year, month).stream()
                .map(salaryMapper::toDTO)
                .toList();
    }
}