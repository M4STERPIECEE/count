package com.budget.backend.application.usecase;

import com.budget.backend.application.dto.SalaryDTO;
import com.budget.backend.application.port.output.RevenueRepositoryPort;
import com.budget.backend.domain.exception.DomainException;
import com.budget.backend.domain.model.Salary;
import com.budget.backend.domain.service.RevenueCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackMonthlySalaryUseCase {

    private final RevenueCalculator revenueCalculator = new RevenueCalculator();

    public BigDecimal calculateTotalSalary(List<SalaryDTO> salaries) {
        return salaries.stream()
                .map(s -> s.getAmount() != null ? s.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateAverageSalary(List<SalaryDTO> salaries, int year) {
        List<SalaryDTO> yearSalaries = salaries.stream()
                .filter(s -> s.getYear() == year)
                .toList();

        if (yearSalaries.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = yearSalaries.stream()
                .map(s -> s.getAmount() != null ? s.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(BigDecimal.valueOf(yearSalaries.size()), 2, java.math.RoundingMode.HALF_UP);
    }

    public BigDecimal projectAnnualSalary(List<SalaryDTO> salaries, int year) {
        List<SalaryDTO> yearSalaries = salaries.stream()
                .filter(s -> s.getYear() == year)
                .toList();

        if (yearSalaries.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal monthlyTotal = yearSalaries.stream()
                .filter(s -> "MONTHLY".equals(s.getType().name()))
                .map(s -> s.getAmount() != null ? s.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal annualTotal = yearSalaries.stream()
                .filter(s -> "ANNUAL".equals(s.getType().name()))
                .map(s -> s.getAmount() != null ? s.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return monthlyTotal.multiply(BigDecimal.valueOf(12)).add(annualTotal);
    }
}
