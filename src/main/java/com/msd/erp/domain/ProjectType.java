package com.msd.erp.domain;

public enum ProjectType {
    RESIDENTIAL("Residential"),
    COMERCIAL("Comercial"),
    INDUSTRIAL("Industrial"),
    INTERNAL("Internal");

     private final String description;

    ProjectType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
