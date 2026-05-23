package com.budget.backend.application.usecase;

import com.budget.backend.application.port.output.RevenueRepositoryPort;
import com.budget.backend.domain.model.Revenue;
import com.budget.backend.domain.service.RevenueCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculateAnnualRevenueUseCase {

    private final RevenueRepositoryPort revenueRepository;
    private final RevenueCalculator revenueCalculator = new RevenueCalculator();

    public BigDecimal calculateTotalRevenue(int year) {
        List<Revenue> revenues = revenueRepository.findByYear(year);
        return revenueCalculator.calculateTotalByYear(revenues, year);
    }

    public BigDecimal calculateMonthlyRevenue(int year, int month) {
        List<Revenue> revenues = revenueRepository.findByYear(year);
        return revenueCalculator.calculateTotalByMonth(revenues, year, month);
    }

    public BigDecimal calculateMonthlyAverage(int year) {
        List<Revenue> revenues = revenueRepository.findByYear(year);
        return revenueCalculator.calculateMonthlyAverage(revenues);
    }

    public BigDecimal calculateGrowthRate(int currentYear) {
        List<Revenue> currentYearRevenues = revenueRepository.findByYear(currentYear);
        List<Revenue> previousYearRevenues = revenueRepository.findByYear(currentYear - 1);
        return revenueCalculator.calculateGrowthRate(currentYearRevenues, previousYearRevenues);
    }
}
