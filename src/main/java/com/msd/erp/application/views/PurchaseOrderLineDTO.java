package com.msd.erp.application.views;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class PurchaseOrderLineDTO {
    private Long id;
    private Long purchaseOrderId;
    private Long articleId;
    private Integer quantity;
    private Double totalLineAmount;
    private Double totalLineAmountWithVAT;
    private Double price;
}
