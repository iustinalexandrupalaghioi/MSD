package com.msd.erp.application.views;

import java.time.LocalDate;

import com.msd.erp.domain.Relation;

import lombok.Data;

@Data
public class RentDTO {
    private Relation customer;
    private LocalDate startDate;
    private LocalDate endDate;
}
