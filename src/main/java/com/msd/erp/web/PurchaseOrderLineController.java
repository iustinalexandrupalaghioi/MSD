package com.msd.erp.web;

import com.msd.erp.domain.PurchaseOrderLine;
import com.msd.erp.application.services.PurchaseOrderLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrderLine> updatePurchaseOrderLine(@PathVariable Long id,
            @RequestBody PurchaseOrderLine purchaseOrderLine) {
        return purchaseOrderLineService.updatePurchaseOrderLine(id, purchaseOrderLine)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseOrderLine(@PathVariable Long id) {
        purchaseOrderLineService.deletePurchaseOrderLine(id);
        return ResponseEntity.noContent().build();
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
        List<PurchaseOrderLine> purchaseOrderLines = purchaseOrderLineService.getSalesOrderLinesByArticleId(articleId);
        return ResponseEntity.ok(purchaseOrderLines);
    }
}
