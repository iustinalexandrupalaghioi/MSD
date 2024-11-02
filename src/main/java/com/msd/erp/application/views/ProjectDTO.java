package com.msd.erp.application.views;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private Long customerId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String projectType;
    private BigDecimal budget;
    private Boolean isInBudget;
}
