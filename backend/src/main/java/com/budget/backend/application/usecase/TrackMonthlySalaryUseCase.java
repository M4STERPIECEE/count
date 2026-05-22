package com.budget.backend.application.usecase;

import com.budget.backend.application.port.output.SalaryRepositoryPort;
import com.budget.backend.domain.service.RevenueCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TrackMonthlySalaryUseCase {

    private final SalaryRepositoryPort salaryRepository;
    private final RevenueCalculator revenueCalculator;

    public BigDecimal calculateTotalSalary(int year) {
        return revenueCalculator.calculateTotalSalary(salaryRepository.findByYear(year));
    }

    public BigDecimal calculateAverageSalary(int year) {
        return revenueCalculator.calculateAverageSalary(salaryRepository.findByYear(year));
    }

    public BigDecimal projectAnnualSalary(int year) {
        return revenueCalculator.projectAnnualSalary(salaryRepository.findByYear(year));
    }
}
