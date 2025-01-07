package com.msd.erp.application.workflowTests;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import com.msd.erp.application.computations.RentComputation;
import com.msd.erp.application.services.ArticleService;
import com.msd.erp.application.services.RentLineService;
import com.msd.erp.application.services.RentService;
import com.msd.erp.application.services.VATRateService;
import com.msd.erp.application.views.RentLineDTO;
import com.msd.erp.domain.Article;
import com.msd.erp.domain.Rent;
import com.msd.erp.domain.RentLine;
import com.msd.erp.domain.VATRate;
import com.msd.erp.infrastructure.repositories.RentLineRepository;

class RentLineServiceTest {

    @Mock
    private RentService rentService;

    @Mock
    private ArticleService articleService;

    @Mock
    private VATRateService vatService;

    @Mock
    private RentLineRepository rentLineRepository;

    @InjectMocks
    private RentLineService rentLineService;

    private Rent mockRent;
    private Article mockArticle;
    private VATRate mockVATRate;
    private RentLine mockRentLine;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Define start and end dates
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 10);

        Instant instant1 = startDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Instant instant2 = endDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        
        Date dateStart = Date.from(instant1);
        Date dateEnd = Date.from(instant2);


        mockRent = new Rent();
        mockRent.setRentId(1L);
        mockRent.setStartDate(dateStart);
        mockRent.setEndDate(dateEnd);
        mockRent.setPeriod(ChronoUnit.DAYS.between(instant1, instant2) + 1);

        
        mockVATRate = new VATRate();
        mockVATRate.setVatid(1L);
        mockVATRate.setPercent(20.0);
        
        mockArticle = new Article();
        mockArticle.setArticleid(1L);
        mockArticle.setVatid(mockVATRate);
        
        mockRentLine = new RentLine();
        mockRentLine.setRent(mockRent);
        mockRentLine.setRentLineId(1L);
        mockRentLine.setArticle(mockArticle);
        mockRentLine.setPricePerDay(100.0);
        mockRentLine.setQuantity(2);

        when(rentService.findById(mockRent.getRentId())).thenReturn(Optional.of(mockRent));
        when(rentLineRepository.findById(mockRentLine.getRentLineId())).thenReturn(Optional.of(mockRentLine));
        when(articleService.getArticleById(mockArticle.getArticleid())).thenReturn(Optional.of(mockArticle));
        when(vatService.getVATRateById(mockVATRate.getVatid())).thenReturn(Optional.of(mockVATRate));
    }

    @Test
    void createRentLine_ShouldCreateAndSaveRentLine() {
        when(rentService.findById(mockRent.getRentId())).thenReturn(Optional.of(mockRent));
        when(articleService.getArticleById(mockArticle.getArticleid())).thenReturn(Optional.of(mockArticle));
        when(vatService.getVATRateById(mockVATRate.getVatid())).thenReturn(Optional.of(mockVATRate));
        when(rentLineRepository.save(mockRentLine)).thenReturn(mockRentLine);

        RentLine createdRentLine = rentLineService.createRentLine(mockRentLine);

        assertEquals(mockRent, createdRentLine.getRent());
        assertEquals(mockArticle, createdRentLine.getArticle());
        verify(rentService, times(1)).updateRentHeaderTotals(
                eq(mockRent),
                eq(createdRentLine.getLineAmount()),
                eq(createdRentLine.getLineAmountWithVAT()),
                eq(createdRentLine.getLineAmountWithPenalties()));
        verify(rentLineRepository, times(1)).save(mockRentLine);
    }

    @Test
    void createRentLine_ShouldThrowExceptionWhenRentNotFound() {
        when(rentService.findById(mockRent.getRentId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> rentLineService.createRentLine(mockRentLine));

        assertEquals("404 NOT_FOUND \"Rent not found\"", exception.getMessage());
        verify(rentLineRepository, never()).save(any());
    }

    @Test
    void getRentLineById_ShouldReturnRentLineWhenExists() {
        Optional<RentLine> rentLine = rentLineService.getRentLineById(mockRentLine.getRentLineId());
        assertTrue(rentLine.isPresent());
        assertEquals(mockRentLine.getRentLineId(), rentLine.get().getRentLineId());
        verify(rentLineRepository, times(1)).findById(mockRentLine.getRentLineId());
    }

    @Test
    void getRentLineById_ShouldReturnEmptyWhenNotFound() {
        when(rentLineRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<RentLine> rentLine = rentLineService.getRentLineById(999L);

        assertTrue(rentLine.isEmpty());
        verify(rentLineRepository, times(1)).findById(999L);
    }

    @Test
    void getAllRentLines_ShouldReturnListOfRentLines() {
        when(rentLineRepository.findAll()).thenReturn(List.of(mockRentLine));

        List<RentLine> rentLines = rentLineService.getAllRentLines();

        assertNotNull(rentLines);
        assertEquals(1, rentLines.size());
        verify(rentLineRepository, times(1)).findAll();
    }

    @Test
    void getRentLinesByRentId_ShouldReturnListWhenRentLinesExist() {
        when(rentLineRepository.findByRentId(mockRent.getRentId())).thenReturn(List.of(mockRentLine));

        List<RentLine> rentLines = rentLineService.getRentLinesByRentId(mockRent.getRentId());

        assertNotNull(rentLines);
        assertEquals(1, rentLines.size());
        assertEquals(mockRentLine, rentLines.get(0));
        verify(rentLineRepository, times(1)).findByRentId(mockRent.getRentId());
    }

    @Test
    void deleteRentLineAndUpdateRent_ShouldReturnFalseWhenRentLineNotFound() {
        when(rentLineRepository.findById(anyLong())).thenReturn(Optional.empty());

        boolean deleted = rentLineService.deleteRentLineAndUpdateRent(999L);

        assertFalse(deleted);
        verify(rentLineRepository, never()).delete(any());
        verify(rentService, never()).updateRentHeaderTotals(any(), anyDouble(), anyDouble(), anyDouble());
    }

    @Test
    void updateRentLine_ShouldRecalculateLineAmountsWhenQuantityChanges() {
        when(rentLineRepository.findById(mockRentLine.getRentLineId())).thenReturn(Optional.of(mockRentLine));

        RentLineDTO rentLineDTO = new RentLineDTO();
        rentLineDTO.setQuantity(5);

        double expectedLineAmount = RentComputation.calculateLineAmount(5, mockRentLine.getPricePerDay(), mockRent.getPeriod());
        double expectedLineAmountWithVAT = RentComputation.calculateLineAmountWithVAT(expectedLineAmount,
                mockVATRate.getPercent());
        double expectedLineAmountWithPenalties = RentComputation
                .calculateLineAmountWithPenalties(expectedLineAmountWithVAT, mockRentLine.getPenaltiesAmount());

        when(rentLineRepository.save(any(RentLine.class))).thenAnswer(invocation -> {
            RentLine savedRentLine = invocation.getArgument(0);
            savedRentLine.setQuantity(5);
            savedRentLine.setLineAmount(expectedLineAmount);
            savedRentLine.setLineAmountWithVAT(expectedLineAmountWithVAT);
            savedRentLine.setLineAmountWithPenalties(expectedLineAmountWithPenalties);
            return savedRentLine;
        });

        RentLine updatedRentLine = rentLineService.updateRentLine(mockRentLine.getRentLineId(), rentLineDTO);

        assertEquals(5, updatedRentLine.getQuantity());
        assertEquals(expectedLineAmount, updatedRentLine.getLineAmount());
        assertEquals(expectedLineAmountWithVAT, updatedRentLine.getLineAmountWithVAT());
        assertEquals(expectedLineAmountWithPenalties, updatedRentLine.getLineAmountWithPenalties());
        verify(rentLineRepository, times(1)).save(any(RentLine.class));
    }

    @Test
    void updateRentLine_ShouldRecalculateLineAmountsWhenVATChanges() {

        VATRate newVATRate = new VATRate();
        newVATRate.setVatid(2L);
        newVATRate.setPercent(15.0);

        when(rentLineRepository.findById(mockRentLine.getRentLineId())).thenReturn(Optional.of(mockRentLine));

        when(vatService.getVATRateById(newVATRate.getVatid())).thenReturn(Optional.of(newVATRate));

        RentLineDTO rentLineDTO = new RentLineDTO();
        rentLineDTO.setVat(newVATRate);

        double expectedLineAmount = RentComputation.calculateLineAmount(mockRentLine.getQuantity(),
                mockRentLine.getPricePerDay(), mockRent.getPeriod());
        double expectedLineAmountWithVAT = RentComputation.calculateLineAmountWithVAT(expectedLineAmount, 15.0);
        double expectedLineAmountWithPenalties = RentComputation
                .calculateLineAmountWithPenalties(expectedLineAmountWithVAT, mockRentLine.getPenaltiesAmount());

        when(rentLineRepository.save(any(RentLine.class))).thenAnswer(invocation -> {
            RentLine savedRentLine = invocation.getArgument(0);
            savedRentLine.setLineAmount(expectedLineAmount);
            savedRentLine.setLineAmountWithVAT(expectedLineAmountWithVAT);
            savedRentLine.setLineAmountWithPenalties(expectedLineAmountWithPenalties);
            return savedRentLine;
        });

        RentLine updatedRentLine = rentLineService.updateRentLine(mockRentLine.getRentLineId(), rentLineDTO);

        assertEquals(expectedLineAmount, updatedRentLine.getLineAmount());
        assertEquals(expectedLineAmountWithVAT, updatedRentLine.getLineAmountWithVAT());
        assertEquals(expectedLineAmountWithPenalties, updatedRentLine.getLineAmountWithPenalties());

        verify(rentLineRepository, times(1)).save(any(RentLine.class));
    }

    @Test
    void updateRentLine_ShouldThrowExceptionWhenRentLineNotFound() {
        when(rentLineRepository.findById(mockRentLine.getRentLineId())).thenReturn(Optional.empty());

        RentLineDTO rentLineDTO = new RentLineDTO();
        rentLineDTO.setQuantity(3);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> rentLineService.updateRentLine(mockRentLine.getRentLineId(), rentLineDTO));

        assertEquals("404 NOT_FOUND \"RentLine not found\"", exception.getMessage());
        verify(rentLineRepository, never()).save(any());
    }

    @Test
    void deleteRentLineAndUpdateRent_ShouldDeleteAndUpdateTotals() {
        when(rentLineRepository.findById(mockRentLine.getRentLineId())).thenReturn(Optional.of(mockRentLine));

        boolean deleted = rentLineService.deleteRentLineAndUpdateRent(mockRentLine.getRentLineId());

        assertTrue(deleted);
        verify(rentLineRepository, times(1)).delete(mockRentLine);
        verify(rentService, times(1)).updateRentHeaderTotals(
                eq(mockRent),
                eq(-mockRentLine.getLineAmount()),
                eq(-mockRentLine.getLineAmountWithVAT()),
                eq(-mockRentLine.getLineAmountWithPenalties()));
    }
}
