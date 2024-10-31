package com.msd.erp.application.views;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String categoryName;
    private double VATRate;
}
