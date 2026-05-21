package com.budget.backend.application.port.output;

import com.budget.backend.domain.model.Salary;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SalaryRepositoryPort {

    Salary save(Salary salary);

    Optional<Salary> findById(UUID id);

    List<Salary> findAll();

    List<Salary> findByYear(int year);

    List<Salary> findByYearAndMonth(int year, int month);

    void deleteById(UUID id);

    boolean existsById(UUID id);
}