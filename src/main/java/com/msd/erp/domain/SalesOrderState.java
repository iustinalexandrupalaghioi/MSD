package com.msd.erp.domain;

public enum SalesOrderState {
    NEW("New"),
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelled"),
    SENT("Sent to customer"),
    RETURNED("Returned");

    private final String description;

    SalesOrderState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
