package com.budget.backend.application.usecase;

import com.budget.backend.application.dto.AnnualReportDTO;
import com.budget.backend.application.dto.RevenueDTO;
import com.budget.backend.application.dto.mapper.RevenueMapper;
import com.budget.backend.application.port.output.RevenueRepositoryPort;
import com.budget.backend.domain.model.Revenue;
import com.budget.backend.domain.service.AnnualReportGenerator;
import com.budget.backend.shared.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenerateReportUseCase {

    private final RevenueRepositoryPort revenueRepository;
    private final AnnualReportGenerator reportGenerator;
    private final RevenueMapper revenueMapper;
    

    @Transactional(readOnly = true)
    public AnnualReportDTO generateAnnualReport(int year) {
        List<Revenue> revenues = revenueRepository.findByYear(year);
        var report = reportGenerator.generate(year, revenues, List.of());
        return toDTO(report);
    }

    @Transactional(readOnly = true)
    public AnnualReportDTO generateReportWithComparison(int currentYear) {
        AnnualReportDTO currentReport = generateAnnualReport(currentYear);
        List<Revenue> previousRevenues = revenueRepository.findByYear(currentYear - 1);
        var previousReport = reportGenerator.generate(currentYear - 1, previousRevenues, List.of());

        currentReport.setGrowthRate(previousReport.getGrowthRate());
        return currentReport;
    }

    private AnnualReportDTO toDTO(com.budget.backend.domain.model.AnnualReport report) {
        Map<Integer, AnnualReportDTO.MonthlyReportDTO> monthlyDTOs = new LinkedHashMap<>();
        if (report.getMonthlyBreakdown() != null) {
            report.getMonthlyBreakdown().forEach((month, monthlyReport) ->
                    monthlyDTOs.put(month, AnnualReportDTO.MonthlyReportDTO.builder()
                            .month(month)
                            .monthName(DateUtils.getMonthName(month))
                            .revenue(monthlyReport.getRevenue())
                            .salary(monthlyReport.getSalary())
                            .transactionCount(monthlyReport.getTransactionCount())
                            .percentageOfTotal(monthlyReport.getPercentageOfTotal())
                            .build())
            );
        }

        return AnnualReportDTO.builder()
                .year(report.getYear())
                .totalRevenue(report.getTotalRevenue())
                .totalSalary(report.getTotalSalary())
                .otherIncome(report.getOtherIncome())
                .totalExpenses(report.getTotalExpenses())
                .netIncome(report.getNetIncome())
                .monthlyAverage(report.getMonthlyAverage())
                .bestMonth(report.getBestMonth())
                .worstMonth(report.getWorstMonth())
                .growthRate(report.getGrowthRate())
                .monthlyBreakdown(monthlyDTOs)
                .generatedAt(java.time.LocalDateTime.now())
                .build();
    }
}
