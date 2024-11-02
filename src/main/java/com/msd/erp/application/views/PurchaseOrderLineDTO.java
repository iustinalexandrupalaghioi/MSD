package com.msd.erp.application.views;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PurchaseOrderLineDTO {
    private Long id;
    private Long purchaseOrderId;
    private Long articleId;
    private Integer quantity;
    private BigDecimal totalLineAmount;
    private BigDecimal totalLineAmountWithVAT;
    private BigDecimal price;
}
