package com.msd.erp.web;

import com.msd.erp.domain.SalesOrder;
import com.msd.erp.application.services.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @PostMapping
    public ResponseEntity<SalesOrder> createSalesOrder(@RequestBody SalesOrder salesOrder) {
        SalesOrder createdSalesOrder = salesOrderService.createSalesOrder(salesOrder);
        return ResponseEntity.ok(createdSalesOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesOrder> getSalesOrderById(@PathVariable Long id) {
        return salesOrderService.getSalesOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<SalesOrder>> getAllSalesOrders() {
        List<SalesOrder> salesOrders = salesOrderService.getAllSalesOrders();
        return ResponseEntity.ok(salesOrders);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesOrder> updateSalesOrder(@PathVariable Long id, @RequestBody SalesOrder salesOrder) {
        return salesOrderService.updateSalesOrder(id, salesOrder)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalesOrder(@PathVariable Long id) {
        salesOrderService.deleteSalesOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<SalesOrder>> getSalesOrdersByCustomerId(@PathVariable Long customerId) {
        List<SalesOrder> salesOrders = salesOrderService.getSalesOrdersByCustomerId(customerId);
        return ResponseEntity.ok(salesOrders);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<SalesOrder>> getSalesOrdersByDateRange(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {
        List<SalesOrder> salesOrders = salesOrderService.getSalesOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(salesOrders);
    }
}
