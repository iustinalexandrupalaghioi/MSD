package com.msd.erp.application.computationsTests;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.msd.erp.application.computations.RentComputation;
import com.msd.erp.domain.Article;
import com.msd.erp.domain.Rent;
import com.msd.erp.domain.RentLine;
import com.msd.erp.domain.VATRate;

class RentComputationTest {

    private Rent rent;
    private RentLine rentLine;
    private VATRate vat;

    @BeforeEach
    void setUp() {
       // Initialize rent object
    rent = new Rent();

    // Define start and end dates
    LocalDate startDate = LocalDate.of(2024, 11, 1);
    LocalDate endDate = LocalDate.of(2024, 11, 10);

    // Convert LocalDate to Date (start of the day at midnight)
    Instant instant1 = startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
    Instant instant2 = endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
    
    // Convert to java.util.Date
    Date dateStart = Date.from(instant1);
    Date dateEnd = Date.from(instant2);

    // Calculate the difference in days
    long daysBetween = ChronoUnit.DAYS.between(instant1, instant2);

    // Set the period in the rent object
    rent.setPeriod(daysBetween);

    // Set the start and end date for the rent (now as Date objects)
    rent.setStartDate(dateStart);
    rent.setEndDate(dateEnd);

    // Initialize VAT rate
    vat = new VATRate();
    vat.setPercent(19.0);

    // Initialize rent line
    rentLine = new RentLine();
    rentLine.setRent(rent);
    rentLine.setQuantity(5);
    rentLine.setPricePerDay(10.0);
    rentLine.setPenaltiesAmount(50.0);
     Article article = new Article();
    article.setVatid(vat);
    rentLine.setArticle(article);
    }

    @Test
    void updateRentPeriod_ShouldCalculatePeriodCorrectly() {
        RentComputation.updateRentPeriod(rent);

        assertEquals(10, rent.getPeriod());
    }

    @Test
    void calculateLineAmount_ShouldReturnCorrectAmount() {
        Double lineAmount = RentComputation.calculateLineAmount(5, 10.0, 2L);

        assertEquals(100.0, lineAmount);
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

        assertEquals(450.0, rentLine.getLineAmount());
        assertEquals(535.5, rentLine.getLineAmountWithVAT());
        assertEquals(585.5, rentLine.getLineAmountWithPenalties());
    }
}
