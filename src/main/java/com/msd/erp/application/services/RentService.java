package com.msd.erp.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msd.erp.application.computations.RentComputation;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.application.views.RentDTO;
import com.msd.erp.domain.Rent;
import com.msd.erp.domain.RentLine;
import com.msd.erp.domain.RentState;
import com.msd.erp.domain.Stock;
import com.msd.erp.infrastructure.repositories.RentLineRepository;
import com.msd.erp.infrastructure.repositories.RentRepository;
import com.msd.erp.infrastructure.repositories.StockRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RentService {

    @Autowired
    private RentRepository rentRepository;

     @Autowired
    private RentLineRepository rentLineRepository;
 
    @Autowired
    private StockRepository stockRepository;
     

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


    public boolean validateStockForRent(Long rentId) {
        Rent rent = rentRepository.findById(rentId)
                .orElseThrow(() -> new EntityNotFoundException("Rent with ID " + rentId + " not found."));

        if (!rent.getState().equals(RentState.NEW)) {
            throw new IllegalStateException("Rent is not in a valid state for stock validation.");
        }

        List<RentLine> rentLines = rentLineRepository.findByRentId(rentId);

        for (RentLine rentLine : rentLines) {
            Stock stock = stockRepository.findByArticle(rentLine.getArticle())
                    .orElseThrow(() -> new EntityNotFoundException("Stock for article " + rentLine.getArticle().getArticleid() + " - " + rentLine.getArticle().getName() + " not found."));

            if (stock.getAvailableQuantity() < rentLine.getQuantity()) {
                return false;
            }
        }

        return true;
    }

    // Confirm rent
    public void confirmRent(Long rentId) {
        Optional<Rent> optionalRent = rentRepository.findById(rentId);

        if (optionalRent.isPresent()) {
            Rent existingRent = optionalRent.get();

            if (existingRent.getState() != RentState.NEW) {
                throw new IllegalStateException("Rent can only be confirmed from the NEW state.");
            }

            List<RentLine> rentLines = rentLineRepository.findByRentId(rentId);

            if (rentLines.isEmpty()) {
            throw new IllegalStateException("Rent cannot be confirmed because it has no rent lines.");
        }

            existingRent.setState(RentState.CONFIRMED);
            rentRepository.save(existingRent);
        } else {
            throw new EntityNotFoundException("Rent not found.");
        }
    }

    // Mark rent as sent
    public void markAsSent(Long rentId) {
        Optional<Rent> optionalRent = rentRepository.findById(rentId);

        if (optionalRent.isPresent()) {
            Rent existingRent = optionalRent.get();

            if (existingRent.getState() != RentState.CONFIRMED) {
                throw new IllegalStateException("Rent can only be sent after being confirmed.");
            }

            List<RentLine> rentLines = rentLineRepository.findByRentId(rentId);

            if (rentLines.isEmpty()) {
            throw new IllegalStateException("Rent cannot be confirmed because it has no rent lines.");
        }

            existingRent.setState(RentState.SENT);
            rentRepository.save(existingRent);
        } else {
            throw new EntityNotFoundException("Rent not found.");
        }
    }

    // Mark rent as returned
    public void markAsReturned(Long rentId) {
        Optional<Rent> optionalRent = rentRepository.findById(rentId);

        if (optionalRent.isPresent()) {
            Rent existingRent = optionalRent.get();

            if (existingRent.getState() != RentState.SENT) {
                throw new IllegalStateException("Rent can only be returned after being sent.");
            }

            existingRent.setState(RentState.RETURNED);
            rentRepository.save(existingRent);
        } else {
            throw new EntityNotFoundException("Rent not found.");
        }
    }

    // Cancel rent
    public void cancelRent(Long rentId) {
        Optional<Rent> optionalRent = rentRepository.findById(rentId);

        if (optionalRent.isPresent()) {
            Rent existingRent = optionalRent.get();

            if (existingRent.getState() == RentState.SENT || existingRent.getState() == RentState.RETURNED) {
                throw new IllegalStateException("Rent cannot be cancelled after being sent or returned.");
            }

            
            existingRent.setState(RentState.CANCELLED);
            rentRepository.save(existingRent);
        } else {
            throw new EntityNotFoundException("Rent not found.");
        }
    }

}
