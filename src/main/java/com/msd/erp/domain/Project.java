package com.msd.erp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "project")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @NotNull(message = "CustomerId cannot be null")
    @ManyToOne
    @JoinColumn(name = "_fk_customerId", nullable = false)
    private Relation CustomerId;

    @NotNull(message = "The start date cannot be null")
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @NotNull(message = "The end date cannot be null")
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @NotNull(message = "The project type cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "project_type", nullable = false)
    private ProjectType projectType;

    @NotNull(message = "Budget cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Budget must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Budget must be a valid decimal value with two decimal places")
    @Column(name = "budget", nullable = false)
    private BigDecimal budget;

    @NotNull(message = "'Is in budget' cannot be null")
    @Column(name = "is_in_budget", nullable = false)
    private Boolean isInBudget;
}
