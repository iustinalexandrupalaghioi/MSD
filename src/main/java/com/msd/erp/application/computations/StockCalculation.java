package com.msd.erp.application.computations;

public class StockCalculation {

    public static Integer calculateTechnicalQuantity(Integer availableQuantity, Integer incomingQuantity,
            Integer rentedQuantity) {
        return availableQuantity + incomingQuantity + rentedQuantity;
    }

    public static Integer calculateAvailableQuantity(Integer technicalQuantity, Integer incomingQuantity,
            Integer rentedQuantity) {
        return technicalQuantity - incomingQuantity - rentedQuantity;
    }

}
