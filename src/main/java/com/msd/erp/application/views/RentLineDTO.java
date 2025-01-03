package com.msd.erp.application.views;

import com.msd.erp.domain.Article;
import com.msd.erp.domain.VATRate;

import lombok.Data;

@Data
public class RentLineDTO {
    private Integer quantity;
    private Double pricePerDay;
    private VATRate vat;
    private Article article;
}
