package com.budget.backend.application.port.input;

import com.budget.backend.application.dto.AnnualReportDTO;
import com.budget.backend.application.dto.RevenueDTO;

import java.util.List;
import java.util.UUID;

public interface RevenueServicePort {

    RevenueDTO createRevenue(RevenueDTO revenueDTO);

    RevenueDTO getRevenueById(UUID id);

    List<RevenueDTO> getAllRevenues();

    RevenueDTO updateRevenue(UUID id, RevenueDTO revenueDTO);

    void deleteRevenue(UUID id);

    AnnualReportDTO generateAnnualReport(int year);
}
