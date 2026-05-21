package com.budget.backend.domain.model;

import com.budget.backend.domain.exception.DomainException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Transaction {

    private final UUID id;
    private final BigDecimal amount;
    private final LocalDate date;
    private final String description;
    private final Category category;
    private final TransactionType type;
    private final LocalDateTime createdAt;

    public enum TransactionType {
        INCOME,
        EXPENSE,
        TRANSFER
    }

    public static Transaction create(BigDecimal amount, LocalDate date, String description,
                                     Category category, TransactionType type) {
        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .amount(amount)
                .date(date)
                .description(description)
                .category(category)
                .type(type)
                .createdAt(LocalDateTime.now())
                .build();

        transaction.validate();
        return transaction;
    }

    public void validate() {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Le montant de la transaction doit être positif");
        }
        if (date == null) {
            throw new DomainException("La date de la transaction est obligatoire");
        }
        if (type == null) {
            throw new DomainException("Le type de transaction est obligatoire");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new DomainException("La date de la transaction ne peut pas être dans le futur");
        }
    }

    public boolean isIncome() {
        return type == TransactionType.INCOME;
    }

    public boolean isExpense() {
        return type == TransactionType.EXPENSE;
    }
}
