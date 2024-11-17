package com.msd.erp.application.workflowTests;

import com.msd.erp.application.services.SalesOrderService;
import com.msd.erp.domain.SalesOrder;
import com.msd.erp.domain.Relation;
import com.msd.erp.domain.Project;
import com.msd.erp.infrastructure.repositories.SalesOrderRepository;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.application.computations.OrdersAmountsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SalesOrderServiceTest {

    @Mock
    private SalesOrderRepository salesOrderRepository;

    @Mock
    private DomainValidationService validationService;

    @Mock
    private OrdersAmountsService ordersAmountsService;

    private SalesOrderService salesOrderService;

    private SalesOrder salesOrder;
    private SalesOrder updatedSalesOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        salesOrderService = new SalesOrderService(salesOrderRepository, validationService, ordersAmountsService);

        // Setup salesOrder object for testing
        Relation customer = new Relation();
        Project project = new Project();
        salesOrder = new SalesOrder(1L, customer, project, LocalDateTime.now(), 100.0, 120.0, null);

        // Setup updatedSalesOrder object for testing
        updatedSalesOrder = new SalesOrder(1L, customer, project, LocalDateTime.now(), 200.0, 240.0, null);
    }

    @Test
    void createSalesOrder_shouldReturnCreatedSalesOrder() {
        // Arrange
        when(ordersAmountsService.calculateSaleOrderAmount(salesOrder)).thenReturn(100.0);
        when(ordersAmountsService.calculateSaleOrderAmountWithVAT(salesOrder)).thenReturn(120.0);
        when(salesOrderRepository.save(salesOrder)).thenReturn(salesOrder);

        // Act
        SalesOrder result = salesOrderService.createSalesOrder(salesOrder);

        // Assert
        assertNotNull(result);
        assertEquals(100.0, result.getTotalPrice());
        assertEquals(120.0, result.getTotalPriceWithVAT());
        verify(salesOrderRepository, times(1)).save(salesOrder);
    }

    @Test
    void saveSalesOrder_shouldReturnSavedSalesOrder() {
        // Arrange
        when(salesOrderRepository.save(salesOrder)).thenReturn(salesOrder);

        // Act
        SalesOrder result = salesOrderService.saveSalesOrder(salesOrder);

        // Assert
        assertNotNull(result);
        verify(salesOrderRepository, times(1)).save(salesOrder);
    }

    @Test
    void updateSalesOrder_shouldReturnUpdatedSalesOrder() {
        // Arrange
        when(salesOrderRepository.findById(1L)).thenReturn(Optional.of(salesOrder));
        when(salesOrderRepository.save(updatedSalesOrder)).thenReturn(updatedSalesOrder);

        // Act
        Optional<SalesOrder> result = salesOrderService.updateSalesOrder(1L, updatedSalesOrder);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(200.0, result.get().getTotalPrice());
        assertEquals(240.0, result.get().getTotalPriceWithVAT());
        verify(salesOrderRepository, times(1)).save(updatedSalesOrder);
    }

    @Test
    void updateSalesOrder_shouldReturnEmptyWhenSalesOrderNotFound() {
        // Arrange
        when(salesOrderRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<SalesOrder> result = salesOrderService.updateSalesOrder(1L, updatedSalesOrder);

        // Assert
        assertFalse(result.isPresent());
        verify(salesOrderRepository, never()).save(any(SalesOrder.class));
    }

    @Test
    void deleteSalesOrder_shouldDeleteSalesOrder() {
        // Arrange
        when(salesOrderRepository.existsById(1L)).thenReturn(true);

        // Act
        salesOrderService.deleteSalesOrder(1L);

        // Assert
        verify(salesOrderRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteSalesOrder_shouldThrowExceptionWhenSalesOrderNotFound() {
        // Arrange
        when(salesOrderRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> salesOrderService.deleteSalesOrder(1L));
        verify(salesOrderRepository, never()).deleteById(1L);
    }

    @Test
    void salesOrderExists_shouldReturnTrueWhenExists() {
        // Arrange
        when(salesOrderRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = salesOrderService.salesOrderExists(1L);

        // Assert
        assertTrue(result);
    }

    @Test
    void salesOrderExists_shouldReturnFalseWhenNotExists() {
        // Arrange
        when(salesOrderRepository.existsById(1L)).thenReturn(false);

        // Act
        boolean result = salesOrderService.salesOrderExists(1L);

        // Assert
        assertFalse(result);
    }

    @Test
    void getAllSalesOrders_shouldReturnListOfSalesOrders() {
        // Arrange
        when(salesOrderRepository.findAll()).thenReturn(List.of(salesOrder));

        // Act
        List<SalesOrder> result = salesOrderService.getAllSalesOrders();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getSalesOrderById_shouldReturnSalesOrderWhenExists() {
        // Arrange
        when(salesOrderRepository.findById(1L)).thenReturn(Optional.of(salesOrder));

        // Act
        Optional<SalesOrder> result = salesOrderService.getSalesOrderById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(salesOrder.getSalesOrderId(), result.get().getSalesOrderId());
    }

    @Test
    void getSalesOrderById_shouldReturnEmptyWhenNotFound() {
        // Arrange
        when(salesOrderRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<SalesOrder> result = salesOrderService.getSalesOrderById(1L);

        // Assert
        assertFalse(result.isPresent());
    }
}
