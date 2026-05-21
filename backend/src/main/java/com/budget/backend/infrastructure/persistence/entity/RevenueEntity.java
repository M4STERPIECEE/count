package com.budget.backend.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "revenues")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueEntity {

    @Id private UUID id;

    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal amount;

    @Column(nullable = false, length = 200) private String source;

    @Column(nullable = false) private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false) private CategoryEntity category;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private com.budget.backend.domain.enums.RevenueType type;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
