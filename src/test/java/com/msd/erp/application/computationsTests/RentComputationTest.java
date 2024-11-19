package com.msd.erp.application.computationsTests;

import com.msd.erp.application.computations.RentComputation;
import com.msd.erp.domain.Rent;
import com.msd.erp.domain.RentLine;
import com.msd.erp.domain.VATRate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RentComputationsTest {

    private Rent rent;
    private RentLine rentLine;
    private VATRate vat;

    @BeforeEach
    void setUp() {
        rent = new Rent();
        rent.setStartDate(LocalDate.of(2024, 11, 1));
        rent.setEndDate(LocalDate.of(2024, 11, 10));

        vat = new VATRate();
        vat.setPercent(19.0);

        rentLine = new RentLine();
        rentLine.setQuantity(5);
        rentLine.setPricePerDay(10.0);
        rentLine.setVat(vat);
        rentLine.setPenaltiesAmount(50.0);
    }

    @Test
    void updateRentPeriod_ShouldCalculatePeriodCorrectly() {
        RentComputation.updateRentPeriod(rent);

        assertEquals(9, rent.getPeriod());
    }

    @Test
    void calculateLineAmount_ShouldReturnCorrectAmount() {
        Double lineAmount = RentComputation.calculateLineAmount(5, 10.0);

        assertEquals(50.0, lineAmount);
    }

    @Test
    void calculateLineAmountWithVAT_ShouldReturnCorrectAmountWithVAT() {
        Double lineAmountWithVAT = RentComputation.calculateLineAmountWithVAT(100.0, 19.0);

        assertEquals(119.0, lineAmountWithVAT);
    }

    @Test
    void calculateLineAmountWithPenalties_ShouldReturnCorrectAmountWithPenalties() {
        Double lineAmountWithPenalties = RentComputation.calculateLineAmountWithPenalties(119.0, 50.0);

        assertEquals(169.0, lineAmountWithPenalties);
    }

    @Test
    void computeLineAmounts_ShouldCalculateAllLineAmountsCorrectly() {
        RentComputation.computeLineAmounts(rentLine);

        assertEquals(50.0, rentLine.getLineAmount());
        assertEquals(59.5, rentLine.getLineAmountWithVAT());
        assertEquals(109.5, rentLine.getLineAmountWithPenalties());
    }
}
