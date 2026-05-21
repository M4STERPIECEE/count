package com.budget.backend.application.usecase;

import com.budget.backend.application.dto.AnnualReportDTO;
import com.budget.backend.application.dto.RevenueDTO;
import com.budget.backend.application.dto.mapper.RevenueMapper;
import com.budget.backend.application.port.input.ReportServicePort;
import com.budget.backend.application.port.output.RevenueRepositoryPort;
import com.budget.backend.application.port.output.SalaryRepositoryPort;
import com.budget.backend.domain.model.AnnualReport;
import com.budget.backend.domain.model.Revenue;
import com.budget.backend.domain.model.Salary;
import com.budget.backend.domain.service.AnnualReportGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GenerateReportUseCase implements ReportServicePort {
    
    private final RevenueRepositoryPort revenueRepository;
    private final SalaryRepositoryPort salaryRepository;
    private final AnnualReportGenerator reportGenerator;
    private final RevenueMapper revenueMapper;
    
    @Override
    public AnnualReportDTO generateAnnualReport(int year) {
        return generateReportWithComparison(year);
    }

    @Override
    public AnnualReportDTO generateReportWithComparison(int year) {
        log.info("Génération du rapport annuel pour l'année {}", year);
        
        List<Revenue> currentYearRevenues = revenueRepository.findByYear(year);
        List<Revenue> previousYearRevenues = revenueRepository.findByYear(year - 1);
        List<Salary> currentYearSalaries = salaryRepository.findByYear(year);

        List<Revenue> revenuesForComparison = new ArrayList<>(currentYearRevenues);
        revenuesForComparison.addAll(previousYearRevenues);

        AnnualReport report = reportGenerator.generate(year, revenuesForComparison, currentYearSalaries);
        
        return mapToDTO(report);
    }
    
    @Override
    public AnnualReportDTO generateMonthlyReport(int year, int month) {
        log.info("Génération du rapport mensuel pour {}/{}", month, year);
        
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Revenue> monthlyRevenues = revenueRepository.findByDateRange(startDate, endDate);
        List<Salary> monthlySalaries = salaryRepository.findByYearAndMonth(year, month);

        AnnualReport monthlyReport = AnnualReport.builder()
            .year(year)
            .totalRevenue(monthlyRevenues.stream()
                .map(Revenue::getAmount)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add))
            .totalSalary(monthlySalaries.stream()
                .map(Salary::getAmount)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add))
            .build();
        
        return mapToDTO(monthlyReport);
    }
    
    private AnnualReportDTO mapToDTO(AnnualReport report) {
        if (report == null) {
            return null;
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
                .monthlyBreakdown(mapMonthlyBreakdown(report.getMonthlyBreakdown()))
                .topRevenues(mapTopRevenues(report.getTopRevenues()))
                .generatedAt(LocalDateTime.now())
                .build();
    }
    
    private java.util.Map<Integer, AnnualReportDTO.MonthlyReportDTO> mapMonthlyBreakdown(
            java.util.Map<Integer, AnnualReport.MonthlyReport> breakdown) {
        
        if (breakdown == null) {
            return null;
        }
        
        return breakdown.entrySet().stream()
                .collect(Collectors.toMap(
                        java.util.Map.Entry::getKey,
                        entry -> {
                            AnnualReport.MonthlyReport mr = entry.getValue();
                            return AnnualReportDTO.MonthlyReportDTO.builder()
                                    .month(mr.getMonth()).monthName(Month.of(mr.getMonth()).getDisplayName(TextStyle.FULL, Locale.FRENCH)).revenue(mr.getRevenue()).salary(mr.getSalary()).transactionCount(mr.getTransactionCount()).percentageOfTotal(mr.getPercentageOfTotal()).build();
                        }
                ));
    }
    
    private List<RevenueDTO> mapTopRevenues(List<Revenue> topRevenues) {
        if (topRevenues == null) {
            return null;
        }
        
        return topRevenues.stream().map(revenueMapper::toDTO).collect(Collectors.toList());
    }
}