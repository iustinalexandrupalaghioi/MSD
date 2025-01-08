package com.msd.erp.web;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.msd.erp.application.services.SalesOrderService;
import com.msd.erp.domain.SalesOrder;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

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

    // Confirm sales order
    @PutMapping("/confirm/{salesOrderId}")
    public ResponseEntity<String> confirmSalesOrder(@PathVariable Long salesOrderId) {
        try {
            boolean isStockValid = salesOrderService.validateStockForSalesOrder(salesOrderId);

            if (isStockValid) {
                salesOrderService.confirmSalesOrder(salesOrderId);
                return new ResponseEntity<>("Stock is sufficient for confirmation.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Not enough stock available.", HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Sales order with id " + salesOrderId + " not found.", HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>("Sales order with id " + salesOrderId + " is not in a valid state for stock validation.", HttpStatus.BAD_REQUEST);
        }
    }

    // Mark sales order as sent
    @PutMapping("/send/{salesOrderId}")
    public ResponseEntity<Void> markAsSent(@PathVariable Long salesOrderId) {
        try {
            salesOrderService.markAsSent(salesOrderId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Mark sales order as delivered
    @PutMapping("/deliver/{salesOrderId}")
    public ResponseEntity<Void> markAsDelivered(@PathVariable Long salesOrderId) {
        try {
            salesOrderService.markAsDelivered(salesOrderId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Cancel sales order
    @PutMapping("/cancel/{salesOrderId}")
    public ResponseEntity<Void> cancelSalesOrder(@PathVariable Long salesOrderId) {
        try {
            salesOrderService.cancelSalesOrder(salesOrderId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
