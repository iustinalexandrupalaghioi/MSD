package com.msd.erp.application.computationsTests;

import com.msd.erp.application.computations.ProjectLineComputation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProjectLineComputationTest {

    @Test
    void calculateLineAmount_ShouldReturnCorrectLineAmount() {

        Integer quantity = 5;
        Double price = 20.0;

        Double result = ProjectLineComputation.calculateLineAmount(quantity, price);

        assertEquals(100.0, result);
    }

    @Test
    void calculateLineAmount_ShouldHandleZeroQuantity() {

        Integer quantity = 0;
        Double price = 20.0;

        Double result = ProjectLineComputation.calculateLineAmount(quantity, price);

        assertEquals(0.0, result);
    }
}
