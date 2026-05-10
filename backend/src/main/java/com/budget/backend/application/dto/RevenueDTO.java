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
@Schema(description = "DTO for revenues")
public class RevenueDTO {

    @Schema(description = "Unique revenue ID", example = "123e4567-e89b-12d3-a456-426614174000") private UUID id;
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Schema(description = "Revenue amount", example = "5000.00", minimum = "0.01") private BigDecimal amount;
    @NotBlank(message = "Source is required") @Size(max = 200, message = "Source must not exceed 200 characters") @Schema(description = "Revenue source", example = "Company XYZ salary") private String source;
    @NotNull(message = "Date is required") @PastOrPresent(message = "Date cannot be in the future") @Schema(description = "Revenue date", example = "2024-01-15") private LocalDate date;
    @NotNull(message = "Category is required") @Schema(description = "Category ID") private UUID categoryId;
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    @Schema(description = "Revenue description", example = "Monthly salary January 2024") private String description;
    @NotNull(message = "Revenue type is required")
    @Schema(description = "Revenue type") private RevenueType type;
    @Schema(description = "Creation date") private LocalDateTime createdAt;
    @Schema(description = "Last update date") private LocalDateTime updatedAt;
}