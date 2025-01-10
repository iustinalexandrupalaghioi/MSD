package com.msd.erp.domain;

public enum PurchaseOrderState {
    NEW("New"),
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelled"),
    SENT("Sent to supplier"),
    RECEIVED("Received");

    private final String description;

    PurchaseOrderState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
