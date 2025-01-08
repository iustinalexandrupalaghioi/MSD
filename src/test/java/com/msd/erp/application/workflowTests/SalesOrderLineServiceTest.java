package com.msd.erp.application.workflowTests;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.msd.erp.application.services.SalesOrderLineService;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.Article;
import com.msd.erp.domain.SalesOrder;
import com.msd.erp.domain.SalesOrderLine;
import com.msd.erp.infrastructure.repositories.SalesOrderLineRepository;

class SalesOrderLineServiceTest {

    @Mock
    private SalesOrderLineRepository salesOrderLineRepository;

    @Mock
    private DomainValidationService validationService;

    @InjectMocks
    private SalesOrderLineService salesOrderLineService;

    private SalesOrderLine salesOrderLine;
    private SalesOrder salesOrder;
    private Article article;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        salesOrder = new SalesOrder();
        salesOrder.setSalesOrderId(1L);
        salesOrder.setTotalPrice(200.0);
        salesOrder.setTotalPriceWithVAT(240.0);

        article = new Article();
        article.setArticleid(1L);
        article.setName("Test Article");

        salesOrderLine = new SalesOrderLine();
        salesOrderLine.setSalesOrderLineId(1L);
        salesOrderLine.setSalesOrder(salesOrder);
        salesOrderLine.setArticle(article);
        salesOrderLine.setQuantity(5);
        salesOrderLine.setPrice(40.0);
        salesOrderLine.setTotalLineAmount(200.0);
        salesOrderLine.setTotalLineAmountWithVAT(240.0);
    }

    @Test
    void createSalesOrderLine_ShouldSaveSalesOrderLine() {
        when(salesOrderLineRepository.save(salesOrderLine)).thenReturn(salesOrderLine);

        SalesOrderLine result = salesOrderLineService.createSalesOrderLine(salesOrderLine);

        assertNotNull(result);
        assertEquals(salesOrderLine.getSalesOrderLineId(), result.getSalesOrderLineId());
        verify(validationService).validateEntity(salesOrderLine);
        verify(salesOrderLineRepository).save(salesOrderLine);
    }

    @Test
    void getSalesOrderLineById_ShouldReturnSalesOrderLine() {
        when(salesOrderLineRepository.findById(1L)).thenReturn(Optional.of(salesOrderLine));

        Optional<SalesOrderLine> result = salesOrderLineService.getSalesOrderLineById(1L);

        assertTrue(result.isPresent());
        assertEquals(salesOrderLine.getSalesOrderLineId(), result.get().getSalesOrderLineId());
        verify(salesOrderLineRepository).findById(1L);
    }

    @Test
    void updateSalesOrderLine_ShouldUpdateExistingLine() {
        SalesOrderLine updatedLine = new SalesOrderLine();
        updatedLine.setQuantity(10);
        updatedLine.setPrice(30.0);
        updatedLine.setTotalLineAmount(300.0);
        updatedLine.setTotalLineAmountWithVAT(360.0);

        when(salesOrderLineRepository.findById(1L)).thenReturn(Optional.of(salesOrderLine));
        when(salesOrderLineRepository.save(salesOrderLine)).thenReturn(salesOrderLine);

        SalesOrderLine result = salesOrderLineService.updateSalesOrderLine(1L, updatedLine);

        
        assertEquals(10, result.getQuantity());
        assertEquals(30.0, result.getPrice());
        assertEquals(300.0, result.getTotalLineAmount());
        assertEquals(360.0, result.getTotalLineAmountWithVAT());
        verify(validationService).validateEntity(salesOrderLine);
        verify(salesOrderLineRepository).save(salesOrderLine);
    }

    @Test
    void deleteSalesOrderLine_ShouldDeleteSalesOrderLine() {
        when(salesOrderLineRepository.existsById(1L)).thenReturn(true);

        salesOrderLineService.deleteSalesOrderLine(1L);

        verify(salesOrderLineRepository).deleteById(1L);
    }

    @Test
    void deleteSalesOrderLine_ShouldThrowExceptionIfNotFound() {
        when(salesOrderLineRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> salesOrderLineService.deleteSalesOrderLine(1L));

        assertEquals("Sales order line with ID 1 does not exist.", exception.getMessage());
    }
}
