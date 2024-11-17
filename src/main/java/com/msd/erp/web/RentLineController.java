package com.msd.erp.web;

import com.msd.erp.domain.RentLine;
import com.msd.erp.application.services.RentLineService;
import com.msd.erp.application.views.RentLineDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/rentlines")
public class RentLineController {

    @Autowired
    private RentLineService rentLineService;

    @PostMapping("/create")
    public ResponseEntity<RentLine> createRentLine(@RequestBody RentLine rentLine) {
        RentLine createdRentLine = rentLineService.createRentLine(rentLine);
        return new ResponseEntity<>(createdRentLine, HttpStatus.CREATED);
    }

    @PutMapping("/update/{rentLineId}")
    public ResponseEntity<RentLine> updateRentLine(
            @PathVariable Long rentLineId,
            @RequestBody RentLineDTO rentLineDTO) {
        try {
            RentLine updatedRentLine = rentLineService.updateRentLine(rentLineId, rentLineDTO);
            return new ResponseEntity<>(updatedRentLine, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<RentLine> getRentLineById(@PathVariable Long id) {
        return rentLineService.getRentLineById(id)
                .map(rentLine -> new ResponseEntity<>(rentLine, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/rent/{rentId}")
    public ResponseEntity<List<RentLine>> getRentLinesByRentId(@PathVariable Long rentId) {
        List<RentLine> rentLines = rentLineService.getRentLinesByRentId(rentId);
        if (rentLines.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rentLines);
    }

    @GetMapping
    public ResponseEntity<List<RentLine>> getAllRentLines() {
        List<RentLine> rentLines = rentLineService.getAllRentLines();
        return new ResponseEntity<>(rentLines, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRentLine(@PathVariable Long id) {

        boolean isDeleted = rentLineService.deleteRentLineAndUpdateRent(id);

        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
