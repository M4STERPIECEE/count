package com.budget.backend.application.port.input;

import com.budget.backend.application.dto.AnnualReportDTO;

public interface ReportServicePort {

    AnnualReportDTO generateAnnualReport(int year);

    AnnualReportDTO generateReportWithComparison(int year);

    AnnualReportDTO generateMonthlyReport(int year, int month);
}