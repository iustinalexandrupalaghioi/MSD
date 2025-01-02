package com.msd.erp.domain;

public enum RentState {
    NEW("New"),          
    CONFIRMED("Confirmed"),
    CANCELLED("Cancelled"),    
    SENT("Sent to customer"),         
    RETURNED("Returned");
    
    private final String description;

    RentState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

