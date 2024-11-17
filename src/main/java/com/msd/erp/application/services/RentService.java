package com.msd.erp.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msd.erp.application.computations.RentComputation;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.application.views.RentDTO;
import com.msd.erp.domain.Rent;
import com.msd.erp.infrastructure.repositories.RentRepository;

@Service
public class RentService {

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private DomainValidationService validationService;

    public Optional<Rent> findById(Long rentId) {
        return rentRepository.findById(rentId);
    }

    public List<Rent> findByCustomerId(Long customerId) {
        return rentRepository.findByCustomerId(customerId);
    }

    public List<Rent> findAll() {
        return rentRepository.findAll();
    }

    public Optional<Rent> updateRent(Long rentId, RentDTO rentDTO) {
        Optional<Rent> optionalRent = rentRepository.findById(rentId);

        if (optionalRent.isPresent()) {
            Rent existingRent = optionalRent.get();

            if (rentDTO.getCustomer() != null) {
                existingRent.setCustomer(rentDTO.getCustomer());
            }

            if (rentDTO.getStartDate() != null) {
                existingRent.setStartDate(rentDTO.getStartDate());
                RentComputation.updateRentPeriod(existingRent);
            }

            if (rentDTO.getEndDate() != null) {
                existingRent.setEndDate(rentDTO.getEndDate());
                RentComputation.updateRentPeriod(existingRent);
            }

            Rent updatedRent = rentRepository.save(existingRent);
            return Optional.of(updatedRent);
        }

        return Optional.empty();
    }

    public Rent save(Rent rent) {
        validationService.validateEntity(rent);
        return rentRepository.save(rent);
    }

    public void deleteById(Long rentId) {
        rentRepository.deleteById(rentId);
    }

    public void updateRentHeaderTotals(
            Rent rent, Double newLineAmount, Double newLineAmountWithVAT, Double newLineAmountWithPenalties) {
        updateRentHeaderTotals(rent, 0.0, newLineAmount, 0.0, newLineAmountWithVAT, 0.0,
                newLineAmountWithPenalties);
    }

    public void updateRentHeaderTotals(
            Rent rent, Double oldLineAmount, Double newLineAmount,
            Double oldLineAmountWithVAT, Double newLineAmountWithVAT,
            Double oldLineAmountWithPenalties, Double newLineAmountWithPenalties) {

        rent.setTotalPrice(rent.getTotalPrice() - oldLineAmount + newLineAmount);
        rent.setTotalPriceWithVAT(rent.getTotalPriceWithVAT() - oldLineAmountWithVAT + newLineAmountWithVAT);
        rent.setTotalPriceWithPenalties(
                rent.getTotalPriceWithPenalties() - oldLineAmountWithPenalties + newLineAmountWithPenalties);

    }
}
