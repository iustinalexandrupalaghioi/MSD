package com.msd.erp.application.workflowTests;

import com.msd.erp.application.services.PurchaseOrderLineService;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.Article;
import com.msd.erp.domain.PurchaseOrder;
import com.msd.erp.domain.PurchaseOrderLine;
import com.msd.erp.infrastructure.repositories.PurchaseOrderLineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchaseOrderLineServiceTest {

    @Mock
    private PurchaseOrderLineRepository purchaseOrderLineRepository;

    @Mock
    private DomainValidationService validationService;

    @InjectMocks
    private PurchaseOrderLineService purchaseOrderLineService;

    private PurchaseOrderLine purchaseOrderLine;
    private PurchaseOrder purchaseOrder;
    private Article article;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseOrderId(1L);
        purchaseOrder.setTotalPrice(100.0);
        purchaseOrder.setTotalPriceWithVAT(120.0);

        article = new Article();
        article.setArticleid(1L);
        article.setName("Test Article");

        purchaseOrderLine = new PurchaseOrderLine();
        purchaseOrderLine.setPurchaseOrderLineId(1L);
        purchaseOrderLine.setPurchaseOrder(purchaseOrder);
        purchaseOrderLine.setArticle(article);
        purchaseOrderLine.setQuantity(2);
        purchaseOrderLine.setPrice(50.0);
        purchaseOrderLine.setTotalLineAmount(100.0);
        purchaseOrderLine.setTotalLineAmountWithVAT(120.0);
    }

    @Test
    void createPurchaseOrderLine_ShouldSavePurchaseOrderLine() {
        when(purchaseOrderLineRepository.save(purchaseOrderLine)).thenReturn(purchaseOrderLine);

        PurchaseOrderLine result = purchaseOrderLineService.createPurchaseOrderLine(purchaseOrderLine);

        assertNotNull(result);
        assertEquals(purchaseOrderLine.getPurchaseOrderLineId(), result.getPurchaseOrderLineId());
        verify(validationService).validateEntity(purchaseOrderLine);
        verify(purchaseOrderLineRepository).save(purchaseOrderLine);
    }

    @Test
    void getPurchaseOrderLineById_ShouldReturnPurchaseOrderLine() {
        when(purchaseOrderLineRepository.findById(1L)).thenReturn(Optional.of(purchaseOrderLine));

        Optional<PurchaseOrderLine> result = purchaseOrderLineService.getPurchaseOrderLineById(1L);

        assertTrue(result.isPresent());
        assertEquals(purchaseOrderLine.getPurchaseOrderLineId(), result.get().getPurchaseOrderLineId());
        verify(purchaseOrderLineRepository).findById(1L);
    }

    @Test
    void updatePurchaseOrderLine_ShouldUpdateExistingLine() {
        PurchaseOrderLine updatedLine = new PurchaseOrderLine();
        updatedLine.setQuantity(3);
        updatedLine.setPrice(60.0);
        updatedLine.setTotalLineAmount(180.0);
        updatedLine.setTotalLineAmountWithVAT(216.0);

        when(purchaseOrderLineRepository.findById(1L)).thenReturn(Optional.of(purchaseOrderLine));
        when(purchaseOrderLineRepository.save(purchaseOrderLine)).thenReturn(purchaseOrderLine);

        PurchaseOrderLine result = purchaseOrderLineService.updatePurchaseOrderLine(1L, updatedLine);

        assertEquals(3, result.getQuantity());
        assertEquals(60.0, result.getPrice());
        assertEquals(180.0, result.getTotalLineAmount());
        assertEquals(216.0, result.getTotalLineAmountWithVAT());
        verify(validationService).validateEntity(purchaseOrderLine);
        verify(purchaseOrderLineRepository).save(purchaseOrderLine);
    }

    @Test
    void deletePurchaseOrderLine_ShouldDeletePurchaseOrderLine() {
        when(purchaseOrderLineRepository.existsById(1L)).thenReturn(true);

        purchaseOrderLineService.deletePurchaseOrderLine(1L);

        verify(purchaseOrderLineRepository).deleteById(1L);
    }

    @Test
    void deletePurchaseOrderLine_ShouldThrowExceptionIfNotFound() {
        when(purchaseOrderLineRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> purchaseOrderLineService.deletePurchaseOrderLine(1L));

        assertEquals("Purchase order line with ID 1 does not exist.", exception.getMessage());
    }
}
