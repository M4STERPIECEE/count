package com.budget.backend.infrastructure.persistence.repository;

import com.budget.backend.domain.enums.RevenueType;
import com.budget.backend.infrastructure.persistence.entity.RevenueEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaRevenueRepository extends JpaRepository<RevenueEntity, UUID> {
    
    List<RevenueEntity> findByDateBetween(LocalDate start, LocalDate end);
    
    @Query("SELECT r FROM RevenueEntity r WHERE YEAR(r.date) = :year AND MONTH(r.date) = :month")
    List<RevenueEntity> findByYearAndMonth(@Param("year") int year, @Param("month") int month);
    
    @Query("SELECT r FROM RevenueEntity r WHERE YEAR(r.date) = :year")
    List<RevenueEntity> findByYear(@Param("year") int year);
    
    List<RevenueEntity> findByType(RevenueType type);
    
    List<RevenueEntity> findByCategoryId(UUID categoryId);
    
    Page<RevenueEntity> findAllByOrderByDateDesc(Pageable pageable);
    
    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM RevenueEntity r WHERE YEAR(r.date) = :year AND MONTH(r.date) = :month")
    BigDecimal sumAmountByYearAndMonth(@Param("year") int year, @Param("month") int month);
    
    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM RevenueEntity r WHERE YEAR(r.date) = :year")
    BigDecimal sumAmountByYear(@Param("year") int year);
    
    @Query("SELECT r FROM RevenueEntity r WHERE r.amount >= :minAmount ORDER BY r.amount DESC")
    List<RevenueEntity> findTopByAmountGreaterThanEqual(@Param("minAmount") BigDecimal minAmount, Pageable pageable);
    
    @Query("SELECT MONTH(r.date) as month, SUM(r.amount) as total " +
           "FROM RevenueEntity r WHERE YEAR(r.date) = :year " +
           "GROUP BY MONTH(r.date) ORDER BY MONTH(r.date)")
    List<Object[]> findMonthlyTotalsByYear(@Param("year") int year);
}