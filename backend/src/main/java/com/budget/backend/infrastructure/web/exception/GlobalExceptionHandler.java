package com.budget.backend.infrastructure.web.exception;

import com.budget.backend.domain.exception.DomainException;
import com.budget.backend.domain.exception.RevenueNotFoundException;
import com.budget.backend.domain.exception.InvalidCategoryException;
import com.budget.backend.domain.exception.InvalidRevenueException;
import com.budget.backend.domain.exception.InvalidAmountException;
import com.budget.backend.domain.exception.InvalidSalaryException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RevenueNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRevenueNotFound(RevenueNotFoundException ex, HttpServletRequest request) {
        log.warn("Revenue not found: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex,HttpServletRequest request) {
        log.warn("Domain exception: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler({InvalidCategoryException.class, InvalidRevenueException.class, 
                       InvalidAmountException.class, InvalidSalaryException.class})
    public ResponseEntity<ErrorResponse> handleInvalidDomainException(DomainException ex, HttpServletRequest request) {
        log.warn("Invalid domain data: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Invalid argument: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ValidationError> validationErrors = ex.getBindingResult().getFieldErrors().stream().map(fieldError -> ValidationError.builder().field(fieldError.getField()).message(fieldError.getDefaultMessage()).build()).toList();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Erreur de validation")
                .path(request.getRequestURI())
                .validationErrors(validationErrors)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur interne est survenue", request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder().status(status.value()).error(status.getReasonPhrase()).message(message).path(request.getRequestURI()).build();
        return ResponseEntity.status(status).body(errorResponse);
    }
}
