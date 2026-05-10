package com.budget.backend.domain.exception;

import java.util.UUID;

public class RevenueNotFoundException extends DomainException {

    public RevenueNotFoundException(UUID id) {
        super("Revenu non trouvé avec l'ID: " + id);
    }

    public RevenueNotFoundException(String message) {
        super(message);
    }
}