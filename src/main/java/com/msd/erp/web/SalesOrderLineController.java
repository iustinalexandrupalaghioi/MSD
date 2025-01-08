package com.msd.erp.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msd.erp.application.services.SalesOrderLineService;
import com.msd.erp.domain.SalesOrderLine;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sales-order-lines")
@RequiredArgsConstructor
public class SalesOrderLineController {

    private final SalesOrderLineService salesOrderLineService;

    @PostMapping
    public ResponseEntity<SalesOrderLine> createSalesOrderLine(@RequestBody SalesOrderLine salesOrderLine) {
        SalesOrderLine createdSalesOrderLine = salesOrderLineService.createSalesOrderLine(salesOrderLine);
        return ResponseEntity.ok(createdSalesOrderLine);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesOrderLine> getSalesOrderLineById(@PathVariable Long id) {
        return salesOrderLineService.getSalesOrderLineById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<SalesOrderLine>> getAllSalesOrderLines() {
        List<SalesOrderLine> salesOrderLines = salesOrderLineService.getAllSalesOrderLines();
        return ResponseEntity.ok(salesOrderLines);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesOrderLine> updateSalesOrderLine(@PathVariable Long id,
            @RequestBody SalesOrderLine salesOrderLine) {
        return salesOrderLineService.updateSalesOrderLine(id, salesOrderLine)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalesOrderLine(@PathVariable Long id) {
        salesOrderLineService.deleteSalesOrderLine(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sales-order/{salesOrderId}")
    public ResponseEntity<List<SalesOrderLine>> getSalesOrderLinesBySalesOrderId(@PathVariable Long salesOrderId) {
        List<SalesOrderLine> salesOrderLines = salesOrderLineService.getSalesOrderLinesBySalesOrderId(salesOrderId);
        return ResponseEntity.ok(salesOrderLines);
    }

    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<SalesOrderLine>> getSalesOrderLinesByArticleId(@PathVariable Long articleId) {
        List<SalesOrderLine> salesOrderLines = salesOrderLineService.getSalesOrderLinesByArticleId(articleId);
        return ResponseEntity.ok(salesOrderLines);
    }
}
