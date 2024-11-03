package com.msd.erp.web;

import com.msd.erp.domain.VATRate;
import com.msd.erp.application.services.VATRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vatrates")
@RequiredArgsConstructor
public class VATRateController {

    private final VATRateService vatRateService;

    @PostMapping
    public ResponseEntity<VATRate> createVATRate(@RequestBody VATRate vatRate) {
        VATRate createdVATRate = vatRateService.createVATRate(vatRate);
        return ResponseEntity.ok(createdVATRate);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VATRate> getVATRateById(@PathVariable Long id) {
        return vatRateService.getVATRateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<VATRate>> getAllVATRates() {
        List<VATRate> vatRates = vatRateService.getAllVATRates();
        return ResponseEntity.ok(vatRates);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VATRate> updateVATRate(@PathVariable Long id, @RequestBody VATRate vatRate) {
        return vatRateService.updateVATRate(id, vatRate)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVATRate(@PathVariable Long id) {
        vatRateService.deleteVATRate(id);
        return ResponseEntity.noContent().build();
    }
}
