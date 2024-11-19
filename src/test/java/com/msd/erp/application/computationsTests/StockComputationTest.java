package com.msd.erp.application.computationsTests;

import com.msd.erp.application.computations.StockCalculation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StockComputationTest {

    @Test
    void calculateTechnicalQuantity_ShouldReturnCorrectQuantity() {

        Integer availableQuantity = 100;
        Integer incomingQuantity = 50;
        Integer rentedQuantity = 30;

        Integer result = StockCalculation.calculateTechnicalQuantity(availableQuantity, incomingQuantity,
                rentedQuantity);

        assertEquals(180, result);
    }

    @Test
    void calculateTechnicalQuantity_ShouldHandleNegativeValues() {

        Integer availableQuantity = 100;
        Integer incomingQuantity = -20;
        Integer rentedQuantity = -10;

        Integer result = StockCalculation.calculateTechnicalQuantity(availableQuantity, incomingQuantity,
                rentedQuantity);

        assertEquals(70, result);
    }

    @Test
    void calculateAvailableQuantity_ShouldReturnCorrectQuantity() {

        Integer technicalQuantity = 180;
        Integer incomingQuantity = 50;
        Integer rentedQuantity = 30;

        Integer result = StockCalculation.calculateAvailableQuantity(technicalQuantity, incomingQuantity,
                rentedQuantity);

        assertEquals(100, result);
    }

    @Test
    void calculateAvailableQuantity_ShouldHandleNegativeValues() {

        Integer technicalQuantity = 180;
        Integer incomingQuantity = -20;
        Integer rentedQuantity = -10;

        Integer result = StockCalculation.calculateAvailableQuantity(technicalQuantity, incomingQuantity,
                rentedQuantity);

        assertEquals(210, result);
    }

}
