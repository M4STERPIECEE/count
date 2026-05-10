package com.budget.backend.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class AnnualReport {

    private final int year;
    private final BigDecimal totalRevenue;
    private final BigDecimal totalSalary;
    private final BigDecimal otherIncome;
    private final BigDecimal totalExpenses;
    private final BigDecimal netIncome;
    private final BigDecimal monthlyAverage;
    private final Map<Integer, MonthlyReport> monthlyBreakdown;
    private final List<Revenue> topRevenues;
    private final String bestMonth;
    private final String worstMonth;
    private final BigDecimal growthRate;

    @Getter
    @Builder
    public static class MonthlyReport {
        private final int month;
        private final BigDecimal revenue;
        private final BigDecimal salary;
        private final int transactionCount;
        private final BigDecimal percentageOfTotal;
    }
}