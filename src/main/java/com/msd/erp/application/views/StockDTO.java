package com.msd.erp.application.views;

import com.msd.erp.domain.Article;

import lombok.Data;

@Data
public class StockDTO {
    private Article article;
    private Integer incomingQuantity;
    private Integer rentedQuantity;
    private Integer technicalQuantity;
    private Integer availableQuantity;
    private Integer soldQuantity;

}
