package com.msd.erp.application.views;

import com.msd.erp.domain.Article;
import com.msd.erp.domain.PurchaseOrder;
import com.msd.erp.domain.PurchaseOrderLine;

import lombok.Data;

@Data
public class ProjectLineDTO {

    private Integer quantity;
    private Double price;
    private Article article;
    private PurchaseOrderLine purchaseOrderLine;
    private PurchaseOrder purchaseOrder;
}
