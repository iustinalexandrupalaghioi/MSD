package com.msd.erp.application.views;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PurchaseOrderDTO {
    private Long id;
    private Long customerId;
    private Long projectId;
    private LocalDateTime date;
    private Double totalPrice;
    private Double totalPriceWithVAT;
}
