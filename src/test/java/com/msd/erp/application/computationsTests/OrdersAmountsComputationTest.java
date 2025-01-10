package com.msd.erp.application.computationsTests;

import com.msd.erp.application.computations.OrdersAmountsService;
import com.msd.erp.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OrdersAmountsComputationTest {

    private OrdersAmountsService ordersAmountsService;

    private PurchaseOrderLine mockPurchaseOrderLine;
    private PurchaseOrder mockPurchaseOrder;
    private SalesOrderLine mockSalesOrderLine;
    private SalesOrder mockSalesOrder;
    private Article mockArticle;
    private VATRate mockVATRate;

    @BeforeEach
    void setUp() {
        ordersAmountsService = new OrdersAmountsService();

        mockArticle = mock(Article.class);
        mockVATRate = mock(VATRate.class);
        mockPurchaseOrderLine = mock(PurchaseOrderLine.class);
        mockPurchaseOrder = mock(PurchaseOrder.class);
        mockSalesOrderLine = mock(SalesOrderLine.class);
        mockSalesOrder = mock(SalesOrder.class);

        when(mockArticle.getPrice()).thenReturn(100.0);
        when(mockVATRate.getPercent()).thenReturn(19.0);
        when(mockArticle.getVatid()).thenReturn(mockVATRate);

        when(mockPurchaseOrderLine.getArticle()).thenReturn(mockArticle);
        when(mockPurchaseOrderLine.getQuantity()).thenReturn(2);
        when(mockPurchaseOrderLine.getTotalLineAmount()).thenReturn(200.0);
        when(mockPurchaseOrderLine.getTotalLineAmountWithVAT()).thenReturn(238.0);

        when(mockSalesOrderLine.getArticle()).thenReturn(mockArticle);
        when(mockSalesOrderLine.getQuantity()).thenReturn(3);
    }

    @Test
    void calculatePurchaseLineAmount_ShouldReturnCorrectAmount() {
        Double lineAmount = ordersAmountsService.calculatePurchaseLineAmount(mockPurchaseOrderLine);
        assertEquals(200.0, lineAmount);
    }

    @Test
    void calculatePurchaseLineAmountWithVAT_ShouldReturnCorrectAmountWithVAT() {
        Double lineAmountWithVAT = ordersAmountsService.calculatePurchaseLineAmountWithVAT(mockPurchaseOrderLine);
        assertEquals(238.0, lineAmountWithVAT);
    }

    @Test
    void calculatePurchaseOrderAmount_ShouldReturnTotalAmountForOrder() {
        PurchaseOrderLine line1 = mock(PurchaseOrderLine.class);
        PurchaseOrderLine line2 = mock(PurchaseOrderLine.class);
        Article mockArticle1 = mock(Article.class);
        Article mockArticle2 = mock(Article.class);

        when(line1.getArticle()).thenReturn(mockArticle1);
        when(line2.getArticle()).thenReturn(mockArticle2);

        when(mockArticle1.getPrice()).thenReturn(100.0);
        when(mockArticle2.getPrice()).thenReturn(150.0);

        when(line1.getQuantity()).thenReturn(1);
        when(line2.getQuantity()).thenReturn(1);

        PurchaseOrder mockPurchaseOrder = mock(PurchaseOrder.class);
        // when(mockPurchaseOrder.getPurchaseOrderLines()).thenReturn(List.of(line1, line2));

        // Double totalAmount = ordersAmountsService.calculatePurchaseOrderAmmount(mockPurchaseOrder);

        // assertEquals(250.0, totalAmount);
    }

    @Test
    void calculatePurchaseOrderAmountWithVAT_ShouldReturnTotalAmountWithVAT() {
        Article mockArticle1 = mock(Article.class);
        Article mockArticle2 = mock(Article.class);
        VATRate mockVATRate1 = mock(VATRate.class);
        VATRate mockVATRate2 = mock(VATRate.class);

        when(mockArticle1.getPrice()).thenReturn(100.0);
        when(mockArticle2.getPrice()).thenReturn(150.0);
        when(mockArticle1.getVatid()).thenReturn(mockVATRate1);
        when(mockArticle2.getVatid()).thenReturn(mockVATRate2);
        when(mockVATRate1.getPercent()).thenReturn(19.0);
        when(mockVATRate2.getPercent()).thenReturn(19.0);

        PurchaseOrderLine line1 = mock(PurchaseOrderLine.class);
        PurchaseOrderLine line2 = mock(PurchaseOrderLine.class);

        when(line1.getArticle()).thenReturn(mockArticle1);
        when(line2.getArticle()).thenReturn(mockArticle2);
        when(line1.getQuantity()).thenReturn(1);
        when(line2.getQuantity()).thenReturn(1);

        when(line1.getTotalLineAmount()).thenReturn(100.0);
        when(line2.getTotalLineAmount()).thenReturn(150.0);

        PurchaseOrder mockPurchaseOrder = mock(PurchaseOrder.class);
        // when(mockPurchaseOrder.getPurchaseOrderLines()).thenReturn(List.of(line1, line2));

        // Double totalAmountWithVAT = ordersAmountsService.calculatePurchaseOrderAmountWithVAT(mockPurchaseOrder);

        // assertEquals(297.5, totalAmountWithVAT);
    }

    @Test
    void calculateSalesLineAmount_ShouldReturnCorrectAmount() {
        Double salesLineAmount = ordersAmountsService.calculateSalesLineAmount(mockSalesOrderLine);
        assertEquals(300.0, salesLineAmount);
    }

    @Test
    void calculateSalesLineAmountWithVAT_ShouldReturnCorrectAmountWithVAT() {
        Double salesLineAmountWithVAT = ordersAmountsService.calculateSalesLineAmountWithVAT(mockSalesOrderLine);
        assertEquals(357.0, salesLineAmountWithVAT);
    }

    @Test
    void calculateSaleOrderAmount_ShouldReturnTotalAmountForSalesOrder() {
        SalesOrderLine line1 = mock(SalesOrderLine.class);
        SalesOrderLine line2 = mock(SalesOrderLine.class);

        when(line1.getQuantity()).thenReturn(2);
        when(line2.getQuantity()).thenReturn(1);

        // when(mockSalesOrder.getSalesOrderLines()).thenReturn(List.of(line1, line2));

        when(line1.getArticle()).thenReturn(mockArticle);
        when(line2.getArticle()).thenReturn(mockArticle);

        when(mockArticle.getPrice()).thenReturn(100.0);

        Double totalAmount = ordersAmountsService.calculateSaleOrderAmount(mockSalesOrder);
        assertEquals(300.0, totalAmount);
    }

    @Test
    void calculateSaleOrderAmountWithVAT_ShouldReturnTotalAmountWithVAT() {
        SalesOrderLine line1 = mock(SalesOrderLine.class);
        SalesOrderLine line2 = mock(SalesOrderLine.class);

        when(line1.getQuantity()).thenReturn(2);
        when(line2.getQuantity()).thenReturn(1);

        // when(mockSalesOrder.getSalesOrderLines()).thenReturn(List.of(line1, line2));

        when(line1.getArticle()).thenReturn(mockArticle);
        when(line2.getArticle()).thenReturn(mockArticle);

        when(mockArticle.getPrice()).thenReturn(100.0);

        Double totalAmountWithVAT = ordersAmountsService.calculateSaleOrderAmountWithVAT(mockSalesOrder);
        assertEquals(357.0, totalAmountWithVAT);
    }
}
