package com.msd.erp.application.views;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class SalesOrderLineDTO {
    private Long id;
    private Long salesOrderId;
    private Long articleId;
    private Integer quantity;
    private Double totalLineAmount;
    private Double totalLineAmountWithVAT;
    private Double price;
}
