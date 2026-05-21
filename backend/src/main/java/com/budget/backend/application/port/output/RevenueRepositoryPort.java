package com.budget.backend.application.port.output;

import com.budget.backend.domain.model.Revenue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RevenueRepositoryPort {

    Revenue save(Revenue revenue);

    Optional<Revenue> findById(UUID id);

    List<Revenue> findAll();

    List<Revenue> findByYear(int year);

    List<Revenue> findByDateRange(LocalDate startDate, LocalDate endDate);

    void deleteById(UUID id);

    boolean existsById(UUID id);
}
