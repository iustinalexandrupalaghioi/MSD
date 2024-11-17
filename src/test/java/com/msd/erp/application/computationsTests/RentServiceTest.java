package com.msd.erp.application.computationsTests;

import com.msd.erp.application.computations.RentComputation;
import com.msd.erp.domain.Rent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RentServiceTest {

    private Rent rent;

    @BeforeEach
    void setUp() {
        rent = new Rent();
        rent.setStartDate(LocalDate.of(2024, 11, 1));
        rent.setEndDate(LocalDate.of(2024, 11, 10));
        rent.setTotalPrice(100.0);
        rent.setTotalPriceWithVAT(119.0);
        rent.setTotalPriceWithPenalties(219.0);
    }

    @Test
    void updateRentPeriod_ShouldCalculatePeriodCorrectly() {
        RentComputation.updateRentPeriod(rent);

        assertEquals(9, rent.getPeriod());
    }

}
