package com.budget.backend.domain.service;

import com.budget.backend.domain.model.AnnualReport;
import com.budget.backend.domain.model.Revenue;
import com.budget.backend.domain.model.Salary;
import com.budget.backend.shared.util.DateUtils;
import com.budget.backend.shared.util.MoneyUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnnualReportGenerator {

    private final RevenueCalculator revenueCalculator;

    public AnnualReportGenerator(RevenueCalculator revenueCalculator) {
        this.revenueCalculator = revenueCalculator;
    }

    public AnnualReport generate(int year, List<Revenue> revenues, List<Salary> salaries) {
        BigDecimal totalRevenue = revenueCalculator.calculateTotalByYear(revenues, year);
        BigDecimal totalSalary = calculateTotalSalary(salaries, year);
        BigDecimal otherIncome = totalRevenue.subtract(totalSalary);
        BigDecimal monthlyAverage = calculateMonthlyAverage(totalRevenue);
        Map<Integer, BigDecimal> monthlyBreakdown = revenueCalculator.calculateMonthlyBreakdown(revenues, year);

        String bestMonth = findBestMonth(monthlyBreakdown);
        String worstMonth = findWorstMonth(monthlyBreakdown);
        BigDecimal growthRate = calculateGrowthRate(revenues, year);
        List<Revenue> topRevenues = revenueCalculator.getTopRevenues(revenues, 10);
        Map<Integer, AnnualReport.MonthlyReport> monthlyReports = buildMonthlyReports(monthlyBreakdown, salaries, year, totalRevenue);

        return AnnualReport.builder()
                .year(year)
                .totalRevenue(totalRevenue)
                .totalSalary(totalSalary)
                .otherIncome(otherIncome)
                .totalExpenses(BigDecimal.ZERO)
                .netIncome(totalRevenue)
                .monthlyAverage(monthlyAverage)
                .monthlyBreakdown(monthlyReports)
                .topRevenues(topRevenues)
                .bestMonth(bestMonth)
                .worstMonth(worstMonth)
                .growthRate(growthRate)
                .build();
    }

    private BigDecimal calculateTotalSalary(List<Salary> salaries, int year) {
        return salaries.stream()
                .filter(s -> s.getYear() == year)
                .map(Salary::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateMonthlyAverage(BigDecimal totalRevenue) {
        if (totalRevenue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return totalRevenue.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
    }

    private Map<Integer, AnnualReport.MonthlyReport> buildMonthlyReports(
            Map<Integer, BigDecimal> monthlyBreakdown,
            List<Salary> salaries,
            int year,
            BigDecimal totalRevenue) {

        Map<Integer, List<Salary>> salariesByMonth = salaries.stream()
                .filter(s -> s.getYear() == year)
                .collect(Collectors.groupingBy(Salary::getMonth));

        Map<Integer, AnnualReport.MonthlyReport> reports = new LinkedHashMap<>();

        for (int month = 1; month <= 12; month++) {
            BigDecimal revenue = monthlyBreakdown.getOrDefault(month, BigDecimal.ZERO);
            BigDecimal salary = salariesByMonth.containsKey(month)
                    ? salariesByMonth.get(month).stream()
                    .map(Salary::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    : BigDecimal.ZERO;

            reports.put(month, AnnualReport.MonthlyReport.builder()
                    .month(month)
                    .revenue(revenue)
                    .salary(salary)
                    .transactionCount(0)
                    .percentageOfTotal(MoneyUtils.percentage(revenue, totalRevenue))
                    .build());
        }
        return reports;
    }

    private String findBestMonth(Map<Integer, BigDecimal> monthlyBreakdown) {
        return monthlyBreakdown.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> DateUtils.getMonthName(entry.getKey()))
                .orElse("N/A");
    }

    private String findWorstMonth(Map<Integer, BigDecimal> monthlyBreakdown) {
        return monthlyBreakdown.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(entry -> DateUtils.getMonthName(entry.getKey()))
                .orElse("N/A");
    }

    private BigDecimal calculateGrowthRate(List<Revenue> revenues, int currentYear) {
        List<Revenue> currentYearRevenues = revenues.stream()
                .filter(r -> r.getDate().getYear() == currentYear)
                .toList();
        List<Revenue> previousYearRevenues = revenues.stream()
                .filter(r -> r.getDate().getYear() == currentYear - 1)
                .toList();

        return revenueCalculator.calculateGrowthRate(currentYearRevenues, previousYearRevenues);
    }
}
