package com.msd.erp.application.computations;

//import java.time.temporal.ChronoUnit;
import com.msd.erp.domain.Rent;
import com.msd.erp.domain.RentLine;

public class RentComputation {

    public static void updateRentPeriod(Rent rent) {
        if (rent.getStartDate() != null && rent.getEndDate() != null) {
//            rent.setPeriod(ChronoUnit.DAYS.between(rent.getStartDate(), rent.getEndDate()));
        }
    }

    public static Double calculateLineAmount(Integer quantity, Double pricePerDay) {
        return quantity * pricePerDay;
    }

    public static Double calculateLineAmountWithPenalties(Double lineAmountWithVAT, Double penaltiesAmount) {
        return lineAmountWithVAT + penaltiesAmount;
    }

    public static Double calculateLineAmountWithVAT(Double lineAmount, Double vatPercentage) {
        return lineAmount + (lineAmount * (vatPercentage / 100));
    }

    public static void computeLineAmounts(RentLine rentLine) {
        rentLine.setLineAmount(calculateLineAmount(rentLine.getQuantity(), rentLine.getPricePerDay()));
        rentLine.setLineAmountWithVAT(calculateLineAmountWithVAT(rentLine.getLineAmount(),
                rentLine.getVat().getPercent()));
        rentLine.setLineAmountWithPenalties(calculateLineAmountWithPenalties(rentLine.getLineAmountWithVAT(),
                rentLine.getPenaltiesAmount()));
    }

}
