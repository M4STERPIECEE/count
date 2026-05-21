package com.budget.backend.infrastructure.web.controller;

import com.budget.backend.application.dto.SalaryDTO;
import com.budget.backend.application.usecase.TrackMonthlySalaryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/salaries")
@RequiredArgsConstructor
@Tag(name = "Salaries", description = "API de gestion des salaires")
public class SalaryController {

    private final TrackMonthlySalaryUseCase salaryUseCase;

    @PostMapping
    @Operation(summary = "Ajouter un salaire")
    public ResponseEntity<SalaryDTO> createSalary(@Valid @RequestBody SalaryDTO salaryDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(salaryDTO);
    }

    @GetMapping("/total/{year}")
    @Operation(summary = "Calculer le total des salaires pour une année")
    public ResponseEntity<BigDecimal> getTotalSalary(@PathVariable int year) {
        return ResponseEntity.ok(BigDecimal.ZERO);
    }

    @GetMapping("/average/{year}")
    @Operation(summary = "Calculer la moyenne des salaires pour une année")
    public ResponseEntity<BigDecimal> getAverageSalary(@PathVariable int year) {
        return ResponseEntity.ok(BigDecimal.ZERO);
    }

    @GetMapping("/projection/{year}")
    @Operation(summary = "Projeter le salaire annuel")
    public ResponseEntity<BigDecimal> getProjectedAnnualSalary(@PathVariable int year) {
        return ResponseEntity.ok(BigDecimal.ZERO);
    }
}
