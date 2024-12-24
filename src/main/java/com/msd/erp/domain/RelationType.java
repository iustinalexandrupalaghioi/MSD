package com.msd.erp.domain;

public enum RelationType {
    SUPPLIER("Supplier"),
    CUSTOMER("Customer");

     private final String description;

    RelationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}