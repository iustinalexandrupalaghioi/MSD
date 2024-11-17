package com.msd.erp.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msd.erp.application.computations.RentComputation;
import com.msd.erp.application.services.RentService;
import com.msd.erp.application.views.RentDTO;
import com.msd.erp.domain.Rent;

@RestController
@RequestMapping("/api/rents")
public class RentController {

    @Autowired
    private RentService rentService;

    @PostMapping("/create")
    public ResponseEntity<Rent> createRent(@RequestBody Rent rent) {
        RentComputation.updateRentPeriod(rent);
        Rent createdRent = rentService.save(rent);
        return new ResponseEntity<>(createdRent, HttpStatus.CREATED);
    }

    @PutMapping("/update/{rentId}")
    public ResponseEntity<Rent> updateRent(@PathVariable Long rentId, @RequestBody RentDTO rentDTO) {
        Optional<Rent> updatedRent = rentService.updateRent(rentId, rentDTO);

        return updatedRent.map(rent -> new ResponseEntity<>(rent, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @GetMapping
    public ResponseEntity<List<Rent>> getAllRents() {
        List<Rent> rents = rentService.findAll();
        if (rents.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(rents, HttpStatus.OK);
    }

    @GetMapping("/{rentId}")
    public ResponseEntity<Rent> getRentById(@PathVariable Long rentId) {
        return rentService.findById(rentId)
                .map(rent -> new ResponseEntity<>(rent, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Rent>> getRentsByCustomerId(@PathVariable Long customerId) {
        List<Rent> rents = rentService.findByCustomerId(customerId);

        if (rents.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(rents, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRent(@PathVariable Long id) {
        rentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
