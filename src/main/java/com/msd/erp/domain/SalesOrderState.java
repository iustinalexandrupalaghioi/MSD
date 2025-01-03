package com.msd.erp.domain;

public enum SalesOrderState {
    NEW("New"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered");

    private final String description;

    SalesOrderState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
