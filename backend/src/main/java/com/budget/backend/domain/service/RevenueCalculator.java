package com.budget.backend.domain.service;

import com.budget.backend.domain.model.Revenue;
import com.budget.backend.domain.model.Salary;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RevenueCalculator {

    public BigDecimal calculateTotal(List<Revenue> revenues) {
        return revenues.stream()
                .map(Revenue::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalByYear(List<Revenue> revenues, int year) {
        return revenues.stream()
                .filter(r -> r.getDate().getYear() == year)
                .map(Revenue::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalByMonth(List<Revenue> revenues, int year, int month) {
        return revenues.stream()
                .filter(r -> r.getDate().getYear() == year && r.getDate().getMonthValue() == month)
                .map(Revenue::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateMonthlyAverage(List<Revenue> revenues) {
        if (revenues.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return calculateTotal(revenues)
                .divide(BigDecimal.valueOf(revenues.size()), 2, RoundingMode.HALF_UP);
    }

    public Map<Integer, BigDecimal> calculateMonthlyBreakdown(List<Revenue> revenues, int year) {
        return revenues.stream()
                .filter(r -> r.getDate().getYear() == year)
                .collect(Collectors.groupingBy(
                        r -> r.getDate().getMonthValue(),
                        Collectors.mapping(
                                Revenue::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));
    }

    public List<Revenue> getTopRevenues(List<Revenue> revenues, int limit) {
        return revenues.stream()
                .sorted((r1, r2) -> r2.getAmount().compareTo(r1.getAmount()))
                .limit(limit)
                .toList();
    }

    public BigDecimal calculateGrowthRate(List<Revenue> currentYearRevenues, List<Revenue> previousYearRevenues) {
        BigDecimal currentTotal = calculateTotal(currentYearRevenues);
        BigDecimal previousTotal = calculateTotal(previousYearRevenues);

        if (previousTotal.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return currentTotal.subtract(previousTotal)
                .multiply(BigDecimal.valueOf(100))
                .divide(previousTotal, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateTotalSalary(List<Salary> salaries) {
        return salaries.stream()
                .map(Salary::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateAverageSalary(List<Salary> salaries) {
        if (salaries.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return calculateTotalSalary(salaries)
                .divide(BigDecimal.valueOf(salaries.size()), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal projectAnnualSalary(List<Salary> salaries) {
        return salaries.stream()
                .map(Salary::getAnnualAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
