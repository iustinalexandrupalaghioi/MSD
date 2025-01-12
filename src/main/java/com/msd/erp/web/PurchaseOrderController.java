package com.msd.erp.web;

import com.msd.erp.domain.PurchaseOrder;
import com.msd.erp.application.services.PurchaseOrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @PostMapping
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(@RequestBody PurchaseOrder purchaseOrder) {
        PurchaseOrder createdPurchaseOrder = purchaseOrderService.createPurchaseOrder(purchaseOrder);
        return ResponseEntity.ok(createdPurchaseOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderById(@PathVariable Long id) {
        return purchaseOrderService.getPurchaseOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrder>> getAllPurchaseOrders() {
        List<PurchaseOrder> purchaseOrders = purchaseOrderService.getAllPurchaseOrders();
        return ResponseEntity.ok(purchaseOrders);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrder> updatePurchaseOrder(@PathVariable Long id,
            @RequestBody PurchaseOrder purchaseOrder) {
        return purchaseOrderService.updatePurchaseOrder(id, purchaseOrder)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseOrder(@PathVariable Long id) {
        purchaseOrderService.deletePurchaseOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<PurchaseOrder>> getPurchaseOrdersBySupplierId(@PathVariable Long supplierId) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderService.getPurchaseOrdersBySupplierId(supplierId);
        return ResponseEntity.ok(purchaseOrders);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<PurchaseOrder>> getPurchaseOrdersByDateRange(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {
        List<PurchaseOrder> purchaseOrders = purchaseOrderService.getPurchaseOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(purchaseOrders);
    }

    // Confirm purchase order
    @PutMapping("/confirm/{purchaseOrderId}")
    public ResponseEntity<String> confirmPurchaseOrder(@PathVariable Long purchaseOrderId) {
        try {
            purchaseOrderService.confirmPurchaseOrder(purchaseOrderId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    // Confirm purchase order
    @PutMapping("/receive/{purchaseOrderId}")
    public ResponseEntity<String> receivePurchaseOrder(@PathVariable Long purchaseOrderId) {
        try {
            purchaseOrderService.markAsReceived(purchaseOrderId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Mark purchase order as sent to supplier
    @PutMapping("/send/{purchaseOrderId}")
    public ResponseEntity<Void> markAsSent(@PathVariable Long purchaseOrderId) {
        try {
            purchaseOrderService.markAsSentToSupplier(purchaseOrderId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Mark sales order as received
    @PutMapping("/deliver/{purchaseOrderId}")
    public ResponseEntity<Void> markAsReceived(@PathVariable Long purchaseOrderId) {
        try {
            purchaseOrderService.markAsReceived(purchaseOrderId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Cancel purchase order
    @PutMapping("/cancel/{purchaseOrderId}")
    public ResponseEntity<Void> cancelPurchaseOrder(@PathVariable Long purchaseOrderId) {
        try {
            purchaseOrderService.cancelPurchaseOrder(purchaseOrderId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
