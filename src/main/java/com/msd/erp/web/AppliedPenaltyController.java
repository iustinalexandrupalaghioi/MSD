package com.msd.erp.web;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msd.erp.application.services.AppliedPenaltyService;
import com.msd.erp.domain.AppliedPenalty;

@RestController
@RequestMapping("/api/applied-penalties")
public class AppliedPenaltyController {

    private final AppliedPenaltyService appliedPenaltyService;

   
    public AppliedPenaltyController(AppliedPenaltyService appliedPenaltyService) {
        this.appliedPenaltyService = appliedPenaltyService;
    }

    @PostMapping("/create")
    public ResponseEntity<AppliedPenalty> createPenalty(@RequestBody AppliedPenalty appliedPenalty) {
        AppliedPenalty savedAppliedPenalty = appliedPenaltyService.createPenalty(appliedPenalty);
        return ResponseEntity.ok(savedAppliedPenalty);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AppliedPenalty> updatePenalty(@PathVariable Long id, @RequestBody AppliedPenalty updatedAppliedPenalty) {
        AppliedPenalty savedAppliedPenalty = appliedPenaltyService.updatePenalty(id, updatedAppliedPenalty);
        return ResponseEntity.ok(savedAppliedPenalty);
    }

    @GetMapping
    public ResponseEntity<List<AppliedPenalty>> getAllAppliedPenalties() {
        List<AppliedPenalty> appliedPenalties = appliedPenaltyService.findAll();
        return ResponseEntity.ok(appliedPenalties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppliedPenalty> getAppliedPenaltyById(@PathVariable Long id) {
        Optional<AppliedPenalty> appliedPenalty = appliedPenaltyService.findById(id);
        return appliedPenalty.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/rent-line/{rentLineId}")
    public ResponseEntity<List<AppliedPenalty>> getAppliedPenaltiesByRentLineId(@PathVariable Long rentLineId) {
        List<AppliedPenalty> appliedPenalties = appliedPenaltyService.findByRentLineId(rentLineId);
        return ResponseEntity.ok(appliedPenalties);
    }

    @GetMapping("/rent/{rentId}")
    public ResponseEntity<List<AppliedPenalty>> getAppliedPenaltiesByRentId(@PathVariable Long rentId) {
        List<AppliedPenalty> appliedPenalties = appliedPenaltyService.findByRentId(rentId);
        return ResponseEntity.ok(appliedPenalties);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAppliedPenalty(@PathVariable Long id) {
        appliedPenaltyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
