package com.msd.erp.web;

import com.msd.erp.domain.PurchaseOrderLine;
import com.msd.erp.application.services.PurchaseOrderLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-order-lines")
@RequiredArgsConstructor
public class PurchaseOrderLineController {

    private final PurchaseOrderLineService purchaseOrderLineService;

    @PostMapping
    public ResponseEntity<PurchaseOrderLine> createPurchaseOrderLine(@RequestBody PurchaseOrderLine purchaseOrderLine) {
        PurchaseOrderLine createdPurchaseOrderLine = purchaseOrderLineService
                .createPurchaseOrderLine(purchaseOrderLine);
        return ResponseEntity.ok(createdPurchaseOrderLine);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderLine> getPurchaseOrderLineById(@PathVariable Long id) {
        return purchaseOrderLineService.getPurchaseOrderLineById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrderLine>> getAllPurchaseOrderLines() {
        List<PurchaseOrderLine> purchaseOrderLines = purchaseOrderLineService.getAllPurchaseOrderLines();
        return ResponseEntity.ok(purchaseOrderLines);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PurchaseOrderLine> updateSalesOrderLine(@PathVariable Long id,
                                                               @RequestBody PurchaseOrderLine purchaseOrderLine) {
        try {
            PurchaseOrderLine updatedPurchaseOrderLine = purchaseOrderLineService.updatePurchaseOrderLine(id, purchaseOrderLine);
            return new ResponseEntity<>(updatedPurchaseOrderLine, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseOrderLine(@PathVariable Long id) {
        purchaseOrderLineService.deletePurchaseOrderLine(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRentLine(@PathVariable Long id) {

        boolean isDeleted = purchaseOrderLineService.deletePurchaseOrderLineAndUpdatePurchaseOrder(id);

        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/purchase-order/{purchaseOrderId}")
    public ResponseEntity<List<PurchaseOrderLine>> getPurchaseOrderLinesByPurchaseOrderId(
            @PathVariable Long purchaseOrderId) {
        List<PurchaseOrderLine> purchaseOrderLines = purchaseOrderLineService
                .getPurchaseOrderLinesByPurchaseOrderId(purchaseOrderId);
        return ResponseEntity.ok(purchaseOrderLines);
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<PurchaseOrderLine>> getPurchaseOrderLinesByArticleId(@PathVariable Long articleId) {
        List<PurchaseOrderLine> purchaseOrderLines = purchaseOrderLineService.getPurchaseOrderLinesByArticleId(articleId);
        return ResponseEntity.ok(purchaseOrderLines);
    }
}
