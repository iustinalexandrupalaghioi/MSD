package com.msd.erp.application.computations;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.msd.erp.domain.Rent;
import com.msd.erp.domain.RentLine;

public class RentComputation {

    public static void updateRentPeriod(Rent rent) {
        if (rent.getStartDate() != null && rent.getEndDate() != null) {
        Instant instant1 = rent.getStartDate().toInstant();
        Instant instant2 = rent.getEndDate().toInstant();

        // Calculate the difference in days
        long daysBetween = ChronoUnit.DAYS.between(instant1, instant2);
            rent.setPeriod(daysBetween + 1);
        }
    }

    public static Double calculateLineAmount(Integer quantity, Double pricePerDay, Long days) {
        return quantity * pricePerDay * days;
    }

    public static Double calculateLineAmountWithPenalties(Double lineAmountWithVAT, Double penaltiesAmount) {
        return lineAmountWithVAT + penaltiesAmount;
    }

    public static Double calculateLineAmountWithVAT(Double lineAmount, Double vatPercentage) {
        return lineAmount + (lineAmount * (vatPercentage / 100));
    }

    public static void computeLineAmounts(RentLine rentLine) {
        rentLine.setLineAmount(calculateLineAmount(rentLine.getQuantity(), rentLine.getPricePerDay(), rentLine.getRent().getPeriod()));
        rentLine.setLineAmountWithVAT(calculateLineAmountWithVAT(rentLine.getLineAmount(),
                rentLine.getArticle().getVatid().getPercent()));
        rentLine.setLineAmountWithPenalties(calculateLineAmountWithPenalties(rentLine.getLineAmountWithVAT(),
                rentLine.getPenaltiesAmount()));
    }

}
