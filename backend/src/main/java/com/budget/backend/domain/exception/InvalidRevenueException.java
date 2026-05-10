package com.budget.backend.domain.exception;

public class InvalidRevenueException extends RuntimeException {
    public InvalidRevenueException(String message) {
        super(message);
    }
}
