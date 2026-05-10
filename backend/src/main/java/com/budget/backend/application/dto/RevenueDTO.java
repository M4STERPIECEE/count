package com.budget.backend.application.dto;

import com.budget.backend.domain.enums.RevenueType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO pour les revenus")
public class RevenueDTO {

    @Schema(description = "ID unique du revenu", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à 0")
    @Schema(description = "Montant du revenu", example = "5000.00", minimum = "0.01")
    private BigDecimal amount;

    @NotBlank(message = "La source est obligatoire")
    @Size(max = 200, message = "La source ne doit pas dépasser 200 caractères")
    @Schema(description = "Source du revenu", example = "Salaire entreprise XYZ")
    private String source;

    @NotNull(message = "La date est obligatoire")
    @PastOrPresent(message = "La date ne peut pas être dans le futur")
    @Schema(description = "Date du revenu", example = "2024-01-15")
    private LocalDate date;

    @NotNull(message = "La catégorie est obligatoire")
    @Schema(description = "ID de la catégorie")
    private UUID categoryId;

    @Size(max = 1000, message = "La description ne doit pas dépasser 1000 caractères")
    @Schema(description = "Description du revenu", example = "Salaire mensuel janvier 2024")
    private String description;

    @NotNull(message = "Le type de revenu est obligatoire")
    @Schema(description = "Type de revenu", example = "RECURRING")
    private RevenueType type;

    @Schema(description = "Date de création")
    private LocalDateTime createdAt;

    @Schema(description = "Date de dernière modification")
    private LocalDateTime updatedAt;
}