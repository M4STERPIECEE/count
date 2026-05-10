package com.budget.backend.application.dto;

import com.budget.backend.domain.enums.SalaryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO pour les salaires")
public class SalaryDTO {

    @Schema(description = "ID unique du salaire")
    private UUID id;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à 0")
    @Schema(description = "Montant du salaire", example = "4500.00")
    private BigDecimal amount;

    @NotNull(message = "Le mois est obligatoire")
    @Min(value = 1, message = "Le mois doit être entre 1 et 12")
    @Max(value = 12, message = "Le mois doit être entre 1 et 12")
    @Schema(description = "Mois du salaire", example = "1", minimum = "1", maximum = "12")
    private int month;

    @NotNull(message = "L'année est obligatoire")
    @Min(value = 2000, message = "L'année doit être valide")
    @Schema(description = "Année du salaire", example = "2024")
    private int year;

    @Size(max = 200, message = "Le nom de l'employeur ne doit pas dépasser 200 caractères")
    @Schema(description = "Nom de l'employeur", example = "Entreprise ABC")
    private String employer;

    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    @Schema(description = "Description du salaire")
    private String description;

    @NotNull(message = "Le type de salaire est obligatoire")
    @Schema(description = "Type de salaire", example = "MONTHLY")
    private SalaryType type;

    @Schema(description = "Date de création")
    private LocalDateTime createdAt;

    @Schema(description = "Date de dernière modification")
    private LocalDateTime updatedAt;
}