package com.budget.backend.application.port.input;

import com.budget.backend.application.dto.SalaryDTO;

import java.util.List;
import java.util.UUID;

public interface SalaryServicePort {

    SalaryDTO createSalary(SalaryDTO salaryDTO);

    SalaryDTO updateSalary(UUID id, SalaryDTO salaryDTO);

    void deleteSalary(UUID id);

    SalaryDTO getSalaryById(UUID id);

    List<SalaryDTO> getAllSalaries();

    List<SalaryDTO> getSalariesByYear(int year);

    List<SalaryDTO> getSalariesByMonth(int year, int month);
}