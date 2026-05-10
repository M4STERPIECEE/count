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
@Schema(description = "DTO for salaries")
public class SalaryDTO {

    @Schema(description = "Unique salary ID")
    private UUID id;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Schema(description = "Salary amount", example = "4500.00")
    private BigDecimal amount;

    @NotNull(message = "Month is required")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    @Schema(description = "Salary month", example = "1", minimum = "1", maximum = "12")
    private int month;

    @NotNull(message = "Year is required")
    @Min(value = 2000, message = "Year must be valid")
    @Schema(description = "Salary year", example = "2024")
    private int year;

    @Size(max = 200, message = "Employer name must not exceed 200 characters")
    @Schema(description = "Employer name", example = "ABC Company")
    private String employer;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Schema(description = "Salary description")
    private String description;

    @NotNull(message = "Salary type is required")
    @Schema(description = "Salary type", example = "MONTHLY")
    private SalaryType type;

    @Schema(description = "Creation date")
    private LocalDateTime createdAt;

    @Schema(description = "Last update date")
    private LocalDateTime updatedAt;
}