package com.msd.erp.application.views;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PurchaseOrderDTO {
    private Long id;
    private Long customerId;
    private Long projectId;
    private LocalDateTime date;
    private BigDecimal totalPrice;
    private BigDecimal totalPriceWithVAT;
}
