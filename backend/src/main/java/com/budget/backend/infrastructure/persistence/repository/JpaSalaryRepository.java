package com.budget.backend.infrastructure.persistence.repository;

import com.budget.backend.infrastructure.persistence.entity.SalaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface JpaSalaryRepository extends JpaRepository<SalaryEntity, UUID> {
    
    List<SalaryEntity> findByYear(int year);
    List<SalaryEntity> findByYearAndMonth(int year, int month);
    @Query("SELECT COALESCE(SUM(s.amount), 0) FROM SalaryEntity s WHERE s.year = :year")
    BigDecimal sumAmountByYear(@Param("year") int year);
    @Query("SELECT COALESCE(SUM(s.amount), 0) FROM SalaryEntity s WHERE s.year = :year AND s.month = :month")
    BigDecimal sumAmountByYearAndMonth(@Param("year") int year, @Param("month") int month);
    Optional<SalaryEntity> findFirstByYearAndMonthOrderByCreatedAtDesc(int year, int month);
}
