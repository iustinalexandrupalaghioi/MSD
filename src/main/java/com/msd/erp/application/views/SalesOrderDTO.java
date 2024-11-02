package com.msd.erp.application.views;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SalesOrderDTO {
    private Long id;
    private Long customerId;
    private Long projectId;
    private LocalDateTime date;
    private BigDecimal totalPrice;
    private BigDecimal totalPriceWithVAT;
}
