package com.msd.erp.application.computationsTests;

import org.junit.jupiter.api.Test;
import com.msd.erp.application.computations.PricingService;

import static org.junit.jupiter.api.Assertions.*;

class PricingServiceComputationTest {

    private final PricingService pricingService = new PricingService();

    @Test
    void calculateDiscountedPrice_shouldReturnCorrectDiscountedPrice() {
        Double price = 100.0;
        Double discount = 20.0;

        Double result = pricingService.calculateDiscountedPrice(price, discount);

        assertEquals(80.0, result, 0.01, "The discounted price should be calculated correctly");
    }

    @Test
    void calculateDiscountedPrice_shouldReturnFullPriceWhenDiscountIsZero() {
        Double price = 100.0;
        Double discount = 0.0;

        Double result = pricingService.calculateDiscountedPrice(price, discount);

        assertEquals(100.0, result, 0.01, "The price should remain the same if discount is 0%");
    }

    @Test
    void calculateDiscountedPrice_shouldReturnZeroWhenDiscountIs100() {
        Double price = 100.0;
        Double discount = 100.0;

        Double result = pricingService.calculateDiscountedPrice(price, discount);

        assertEquals(0.0, result, 0.01, "The price should be 0 if discount is 100%");
    }

    @Test
    void calculateDiscountedPrice_shouldThrowExceptionWhenDiscountIsNegative() {
        Double price = 100.0;
        Double discount = -10.0;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pricingService.calculateDiscountedPrice(price, discount)
        );

        assertEquals("Discount percentage must be between 0 and 100", exception.getMessage());
    }

    @Test
    void calculateDiscountedPrice_shouldThrowExceptionWhenDiscountIsAbove100() {
        Double price = 100.0;
        Double discount = 110.0;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> pricingService.calculateDiscountedPrice(price, discount)
        );

        assertEquals("Discount percentage must be between 0 and 100", exception.getMessage());
    }
}
