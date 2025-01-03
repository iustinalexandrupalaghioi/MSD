package com.msd.erp.application.views;

import java.util.Date;

import com.msd.erp.domain.Relation;

import lombok.Data;

@Data
public class RentDTO {
    private Relation customer;
    private Date startDate;
    private Date endDate;
    
}
