package com.msd.erp.application.computations;

import org.springframework.stereotype.Service;

@Service
public class PricingService {

    // Service to calculate price discounts
    public Double calculateDiscountedPrice(Double price, Double discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        return price - (price * discountPercentage / 100);
    }
}
