package com.budget.backend.infrastructure.web.controller;

import com.budget.backend.application.dto.AnnualReportDTO;
import com.budget.backend.application.port.input.RevenueServicePort;
import com.budget.backend.application.usecase.GenerateReportUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "API de génération de rapports financiers")
public class ReportController {

    private final RevenueServicePort revenueService;
    private final GenerateReportUseCase generateReportUseCase;

    @GetMapping("/annual/{year}")
    @Operation(summary = "Générer un rapport annuel")
    public ResponseEntity<AnnualReportDTO> generateAnnualReport(@PathVariable int year) {
        return ResponseEntity.ok(revenueService.generateAnnualReport(year));
    }

    @GetMapping("/annual/{year}/comparison")
    @Operation(summary = "Générer un rapport annuel avec comparaison à l'année précédente")
    public ResponseEntity<AnnualReportDTO> generateReportWithComparison(@PathVariable int year) {
        return ResponseEntity.ok(generateReportUseCase.generateReportWithComparison(year));
    }
}
