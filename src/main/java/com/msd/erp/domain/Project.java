package com.msd.erp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonGetter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Relation customerId;

    @NotNull(message = "The start date cannot be null")
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @NotNull(message = "The end date cannot be null")
    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @NotNull(message = "The project type cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "project_type", nullable = false)
    private ProjectType projectType;

    @NotNull(message = "Budget cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Budget must be greater than 0")
    @Column(name = "budget", nullable = false)
    private Double budget;

    @Column(name = "is_in_budget")
    private Boolean isInBudget;

    @JsonGetter("projectTypeDescription")
    public String getProjectTypeDescription() {
        return projectType.getDescription();
    }
}
