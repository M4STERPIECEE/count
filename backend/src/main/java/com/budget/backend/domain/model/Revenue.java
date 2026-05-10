package com.budget.backend.domain.model;

import com.budget.backend.domain.exception.InvalidAmountException;
import com.budget.backend.domain.exception.InvalidRevenueException;
import com.budget.backend.domain.enums.RevenueType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Revenue {

    private final UUID id;
    private final BigDecimal amount;
    private final String source;
    private final LocalDate date;
    private final Category category;
    private final String description;
    private final RevenueType type;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static Revenue create(BigDecimal amount, String source, LocalDate date,
                                 Category category, String description, RevenueType type) {

        Revenue revenue = Revenue.builder()
                .id(UUID.randomUUID())
                .amount(amount)
                .source(source)
                .date(date)
                .category(category)
                .description(description)
                .type(type)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        revenue.validate();
        return revenue;
    }

    public void validate() {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Le montant doit être positif");
        }

        if (source == null || source.trim().isEmpty()) {
            throw new InvalidRevenueException("La source de revenu est obligatoire");
        }

        if (date == null) {
            throw new InvalidRevenueException("La date est obligatoire");
        }

        if (date.isAfter(LocalDate.now())) {
            throw new InvalidRevenueException("La date ne peut pas être dans le futur");
        }

        if (category == null) {
            throw new InvalidRevenueException("La catégorie est obligatoire");
        }

        if (type == null) {
            throw new InvalidRevenueException("Le type de revenu est obligatoire");
        }

        if (source.length() > 200) {
            throw new InvalidRevenueException("La source ne doit pas dépasser 200 caractères");
        }

        if (description != null && description.length() > 1000) {
            throw new InvalidRevenueException("La description ne doit pas dépasser 1000 caractères");
        }
    }

    public boolean isRecurring() {
        return type == RevenueType.RECURRING;
    }

    public boolean isOneTime() {
        return type == RevenueType.ONE_TIME;
    }
}