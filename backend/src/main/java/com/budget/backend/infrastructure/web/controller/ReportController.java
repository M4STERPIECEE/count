package com.budget.backend.infrastructure.web.controller;

import com.budget.backend.application.dto.AnnualReportDTO;
import com.budget.backend.application.port.input.ReportServicePort;
import com.budget.backend.shared.constant.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(AppConstants.API_V1_PATH + "/reports")
@RequiredArgsConstructor
@Tag(name = "Rapports", description = "API de génération de rapports financiers")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReportController {
    
    private final ReportServicePort reportService;
    
    @Operation(summary = "Générer un rapport annuel", 
              description = "Génère un rapport financier complet pour une année donnée")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rapport généré avec succès",
                    content = @Content(schema = @Schema(implementation = AnnualReportDTO.class))),
        @ApiResponse(responseCode = "400", description = "Année invalide"),
        @ApiResponse(responseCode = "500", description = "Erreur lors de la génération du rapport")
    })
    @GetMapping(value = "/annual/{year}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnnualReportDTO> generateAnnualReport(
            @Parameter(description = "Année pour le rapport", example = "2024") 
            @PathVariable int year) {
        
        log.info("Requête GET pour le rapport annuel de l'année {}", year);
        
        AnnualReportDTO report = reportService.generateAnnualReport(year);
        return ResponseEntity.ok(report);
    }
    
    @Operation(summary = "Générer un rapport mensuel", 
              description = "Génère un rapport financier pour un mois spécifique")
    @GetMapping(value = "/monthly/{year}/{month}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AnnualReportDTO> generateMonthlyReport(
            @Parameter(description = "Année") @PathVariable int year,
            @Parameter(description = "Mois (1-12)") @PathVariable int month) {
        
        log.info("Requête GET pour le rapport mensuel de {}/{}", month, year);
        AnnualReportDTO report = reportService.generateMonthlyReport(year, month);
        return ResponseEntity.ok(report);
    }
}