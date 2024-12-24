package com.msd.erp.domain;

public enum UMType {
    PIECES("Pieces"),
    KILOGRAMS("Kilograms"),
    LITERS("Liters"),
    METERS("Meters"),
    SQUARE_METERS("Square Meters"),
    CUBIC_METERS("Cubic Meters"),
    TONS("Tons");

    private final String description;

    UMType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
