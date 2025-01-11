//package com.msd.erp.application.workflowTests;
//
//import com.msd.erp.application.computations.OrdersAmountsService;
//import com.msd.erp.application.services.PurchaseOrderService;
//import com.msd.erp.application.validations.DomainValidationService;
//import com.msd.erp.domain.PurchaseOrder;
//import com.msd.erp.infrastructure.repositories.PurchaseOrderRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class PurchaseOrderServiceTest {
//
//    @Mock
//    private PurchaseOrderRepository purchaseOrderRepository;
//
//    @Mock
//    private DomainValidationService validationService;
//
//    @Mock
//    private OrdersAmountsService ordersAmountsService;
//
//    @InjectMocks
//    private PurchaseOrderService purchaseOrderService;
//
//    private PurchaseOrder purchaseOrder;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        purchaseOrder = new PurchaseOrder();
//        purchaseOrder.setPurchaseOrderId(1L);
//        purchaseOrder.setDate(LocalDateTime.now());
//        purchaseOrder.setTotalPrice(100.0);
//        purchaseOrder.setTotalPriceWithVAT(120.0);
//        purchaseOrder.setSupplierId(null);
//    }
//
//    @Test
//    void createPurchaseOrder_ShouldSavePurchaseOrder() {
//        when(purchaseOrderRepository.save(purchaseOrder)).thenReturn(purchaseOrder);
//
//        PurchaseOrder result = purchaseOrderService.createPurchaseOrder(purchaseOrder);
//
//        assertNotNull(result);
//        assertEquals(purchaseOrder.getPurchaseOrderId(), result.getPurchaseOrderId());
//        verify(validationService).validateEntity(purchaseOrder);
//        verify(purchaseOrderRepository).save(purchaseOrder);
//    }
//
//    @Test
//    void savePurchaseOrder_ShouldCalculateAndSavePurchaseOrder() {
//        // when(ordersAmountsService.calculatePurchaseOrderAmmount(purchaseOrder)).thenReturn(100.0);
//        // when(ordersAmountsService.calculatePurchaseOrderAmountWithVAT(purchaseOrder)).thenReturn(120.0);
//        when(purchaseOrderRepository.save(purchaseOrder)).thenReturn(purchaseOrder);
//
//        PurchaseOrder result = purchaseOrderService.savePurchaseOrder(purchaseOrder);
//
//        assertNotNull(result);
//        assertEquals(100.0, result.getTotalPrice());
//        assertEquals(120.0, result.getTotalPriceWithVAT());
//        // verify(ordersAmountsService).calculatePurchaseOrderAmmount(purchaseOrder);
//        // verify(ordersAmountsService).calculatePurchaseOrderAmountWithVAT(purchaseOrder);
//        verify(purchaseOrderRepository).save(purchaseOrder);
//    }
//
//    @Test
//    void getPurchaseOrderById_ShouldReturnPurchaseOrder() {
//        when(purchaseOrderRepository.findById(1L)).thenReturn(Optional.of(purchaseOrder));
//
//        Optional<PurchaseOrder> result = purchaseOrderService.getPurchaseOrderById(1L);
//
//        assertTrue(result.isPresent());
//        assertEquals(purchaseOrder.getPurchaseOrderId(), result.get().getPurchaseOrderId());
//        verify(purchaseOrderRepository).findById(1L);
//    }
//
//    @Test
//    void updatePurchaseOrder_ShouldUpdateExistingOrder() {
//        PurchaseOrder updatedOrder = new PurchaseOrder();
//        updatedOrder.setDate(LocalDateTime.now());
//        updatedOrder.setTotalPrice(200.0);
//        updatedOrder.setTotalPriceWithVAT(240.0);
//
//        when(purchaseOrderRepository.findById(1L)).thenReturn(Optional.of(purchaseOrder));
//        when(purchaseOrderRepository.save(purchaseOrder)).thenReturn(purchaseOrder);
//
//        Optional<PurchaseOrder> result = purchaseOrderService.updatePurchaseOrder(1L, updatedOrder);
//
//        assertTrue(result.isPresent());
//        assertEquals(200.0, result.get().getTotalPrice());
//        assertEquals(240.0, result.get().getTotalPriceWithVAT());
//        verify(validationService).validateEntity(purchaseOrder);
//        verify(purchaseOrderRepository).save(purchaseOrder);
//    }
//
//    @Test
//    void deletePurchaseOrder_ShouldDeletePurchaseOrder() {
//        when(purchaseOrderRepository.existsById(1L)).thenReturn(true);
//
//        purchaseOrderService.deletePurchaseOrder(1L);
//
//        verify(purchaseOrderRepository).deleteById(1L);
//    }
//
//    @Test
//    void deletePurchaseOrder_ShouldThrowExceptionIfNotFound() {
//        when(purchaseOrderRepository.existsById(1L)).thenReturn(false);
//
//        Exception exception = assertThrows(IllegalArgumentException.class,
//                () -> purchaseOrderService.deletePurchaseOrder(1L));
//
//        assertEquals("Purchase order with ID 1 does not exist.", exception.getMessage());
//    }
//}
