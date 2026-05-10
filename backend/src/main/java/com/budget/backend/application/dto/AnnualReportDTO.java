package com.budget.backend.application.dto;

import com.budget.backend.domain.model.AnnualReport;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for annual report")
public class AnnualReportDTO {

    private int year;
    private BigDecimal totalRevenue;
    private BigDecimal totalSalary;
    private BigDecimal otherIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netIncome;
    private BigDecimal monthlyAverage;
    private String bestMonth;
    private String worstMonth;
    private BigDecimal growthRate;
    private Map<Integer, MonthlyReportDTO> monthlyBreakdown;
    private List<RevenueDTO> topRevenues;
    private LocalDateTime generatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyReportDTO {
        private int month;
        private String monthName;
        private BigDecimal revenue;
        private BigDecimal salary;
        private int transactionCount;
        private BigDecimal percentageOfTotal;
    }
}