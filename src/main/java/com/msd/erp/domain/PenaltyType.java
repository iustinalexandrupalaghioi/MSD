package com.msd.erp.domain;

public enum PenaltyType {
    LATE_PAYMENT("Late payment"),
    LATE_RETURN("Late return"),
    LOST_ARTICLE("Lost article");


    private final String description;

    PenaltyType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
