package com.budget.backend.application.usecase;

import com.budget.backend.application.dto.AnnualReportDTO;
import com.budget.backend.application.dto.RevenueDTO;
import com.budget.backend.application.dto.mapper.RevenueMapper;
import com.budget.backend.application.port.input.RevenueServicePort;
import com.budget.backend.application.port.output.NotificationPort;
import com.budget.backend.application.port.output.RevenueRepositoryPort;
import com.budget.backend.domain.exception.RevenueNotFoundException;
import com.budget.backend.domain.model.Revenue;
import com.budget.backend.domain.service.AnnualReportGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManageRevenueUseCase implements RevenueServicePort {

    private final RevenueRepositoryPort revenueRepository;
    private final NotificationPort notificationPort;
    private final RevenueMapper revenueMapper;
    private final AnnualReportGenerator annualReportGenerator;

    @Override
    @Transactional
    public RevenueDTO createRevenue(RevenueDTO revenueDTO) {
        Revenue revenue = revenueMapper.toDomain(revenueDTO);
        Revenue savedRevenue = revenueRepository.save(revenue);
        notificationPort.info("Nouveau revenu créé : " + savedRevenue.getSource());
        return revenueMapper.toDTO(savedRevenue);
    }

    @Override
    @Transactional(readOnly = true)
    public RevenueDTO getRevenueById(UUID id) {
        return revenueRepository.findById(id)
                .map(revenueMapper::toDTO)
                .orElseThrow(() -> new RevenueNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RevenueDTO> getAllRevenues() {
        return revenueRepository.findAll().stream()
                .map(revenueMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public RevenueDTO updateRevenue(UUID id, RevenueDTO revenueDTO) {
        Revenue existingRevenue = revenueRepository.findById(id)
                .orElseThrow(() -> new RevenueNotFoundException(id));

        Revenue updatedRevenue = Revenue.builder()
                .id(existingRevenue.getId())
                .amount(revenueDTO.getAmount())
                .source(revenueDTO.getSource())
                .date(revenueDTO.getDate())
                .category(revenueMapper.categoryFromId(revenueDTO.getCategoryId()))
                .description(revenueDTO.getDescription())
                .type(revenueDTO.getType())
                .createdAt(existingRevenue.getCreatedAt())
                .updatedAt(java.time.LocalDateTime.now())
                .build();
        updatedRevenue.validate();

        Revenue savedRevenue = revenueRepository.save(updatedRevenue);
        notificationPort.info("Revenu modifié : " + savedRevenue.getSource());
        return revenueMapper.toDTO(savedRevenue);
    }

    @Override
    @Transactional
    public void deleteRevenue(UUID id) {
        if (!revenueRepository.existsById(id)) {
            throw new RevenueNotFoundException(id);
        }
        revenueRepository.deleteById(id);
        notificationPort.warn("Revenu supprimé avec l'ID : " + id);
    }

    @Override
    @Transactional(readOnly = true)
    public AnnualReportDTO generateAnnualReport(int year) {
        List<Revenue> revenues = revenueRepository.findByYear(year);
        var report = annualReportGenerator.generate(year, revenues, List.of());
        return toAnnualReportDTO(report);
    }

    private AnnualReportDTO toAnnualReportDTO(com.budget.backend.domain.model.AnnualReport report) {
        List<RevenueDTO> topRevenueDTOs = report.getTopRevenues().stream()
                .map(revenueMapper::toDTO)
                .toList();

        java.util.Map<Integer, AnnualReportDTO.MonthlyReportDTO> monthlyDTOs = new java.util.LinkedHashMap<>();
        if (report.getMonthlyBreakdown() != null) {
            report.getMonthlyBreakdown().forEach((month, monthlyReport) ->
                    monthlyDTOs.put(month, AnnualReportDTO.MonthlyReportDTO.builder()
                            .month(month)
                            .monthName(com.budget.backend.shared.util.DateUtils.getMonthName(month))
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
                .topRevenues(topRevenueDTOs)
                .generatedAt(java.time.LocalDateTime.now())
                .build();
    }
}
