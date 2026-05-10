package com.budget.backend.domain.model;

import com.budget.backend.domain.exception.InvalidSalaryException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Salary {

    private final UUID id;
    private final BigDecimal amount;
    private final int month;
    private final int year;
    private final String employer;
    private final String description;
    private final SalaryType type;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static Salary create(BigDecimal amount, int month, int year,
                                String employer, String description, SalaryType type) {

        Salary salary = Salary.builder()
                .id(UUID.randomUUID())
                .amount(amount)
                .month(month)
                .year(year)
                .employer(employer)
                .description(description)
                .type(type)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        salary.validate();
        return salary;
    }

    public void validate() {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidSalaryException("Le montant du salaire doit être positif");
        }

        if (month < 1 || month > 12) {
            throw new InvalidSalaryException("Le mois doit être entre 1 et 12");
        }

        if (year < 2000 || year > 2100) {
            throw new InvalidSalaryException("L'année n'est pas valide");
        }

        if (employer != null && employer.length() > 200) {
            throw new InvalidSalaryException("Le nom de l'employeur ne doit pas dépasser 200 caractères");
        }
    }

    public BigDecimal getAnnualAmount() {
        if (type == SalaryType.MONTHLY) {
            return amount.multiply(BigDecimal.valueOf(12));
        }
        return amount;
    }
}