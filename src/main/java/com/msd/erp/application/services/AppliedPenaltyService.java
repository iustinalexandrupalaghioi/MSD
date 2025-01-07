package com.msd.erp.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.AppliedPenalty;
import com.msd.erp.domain.Penalty;
import com.msd.erp.domain.RentLine;
import com.msd.erp.infrastructure.repositories.AppliedPenaltyRepository;

@Service
public class AppliedPenaltyService {

    @Autowired
    private AppliedPenaltyRepository appliedPenaltyRepository;

    @Autowired
    private RentLineService rentLineService;

    @Autowired
    private PenaltyService penaltyService;

  

    @Autowired
    private DomainValidationService validationService;

    public AppliedPenalty createPenalty(AppliedPenalty appliedPenalty) {
        Long rentLineId = appliedPenalty.getRentLine().getRentLineId();
        Long penaltyId = appliedPenalty.getPenalty().getPenaltyid();

        
        RentLine rentLine = rentLineService.getRentLineById(rentLineId)
                .orElseThrow(() -> new IllegalArgumentException("RentLine with ID " + rentLineId + " does not exist."));

        Penalty penalty = penaltyService.getPenaltyById(penaltyId)
                .orElseThrow(() -> new IllegalArgumentException("Penalty with ID " + penaltyId + " does not exist."));

        appliedPenalty.setPenalty(penalty);
        appliedPenalty.setRentLine(rentLine);

        Double penaltyPrice = penalty.getPrice();

        if (penaltyPrice == null) {
            throw new IllegalArgumentException("Penalty price must not be null.");
        }

        double oldPenaltiesAmount = rentLine.getPenaltiesAmount();
        rentLine.setPenaltiesAmount(oldPenaltiesAmount + penaltyPrice);

        rentLineService.updateRentLine(rentLine);

        return appliedPenaltyRepository.save(appliedPenalty);

    }

    public AppliedPenalty updatePenalty(Long id, AppliedPenalty appliedPenalty) {
        AppliedPenalty existingAppliedPenalty = appliedPenaltyRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("AppliedPenalty with ID " + id + " does not exist."));

        // Retrieve related entities
        Long rentLineId = appliedPenalty.getRentLine().getRentLineId();
        Long penaltyId = appliedPenalty.getPenalty().getPenaltyid();

     
        Penalty penalty = penaltyService.getPenaltyById(penaltyId)
            .orElseThrow(() -> new IllegalArgumentException("Penalty with ID " + penaltyId + " does not exist."));
        RentLine rentLine = rentLineService.getRentLineById(rentLineId)
            .orElseThrow(() -> new IllegalArgumentException("RentLine with ID " + rentLineId + " does not exist."));

        Double oldPenaltyPrice = existingAppliedPenalty.getPenalty().getPrice();
        Double newPenaltyPrice = penalty.getPrice();

        if (newPenaltyPrice == null) {
        throw new IllegalArgumentException("New penalty price must not be null.");
        }

        Double adjustedPenaltiesAmount = rentLine.getPenaltiesAmount() - oldPenaltyPrice + newPenaltyPrice;
        rentLine.setPenaltiesAmount(adjustedPenaltiesAmount);
        rentLineService.updateRentLine(rentLine);

        // Update AppliedPenalty fields
        existingAppliedPenalty.setPenalty(penalty);
        existingAppliedPenalty.setRentLine(rentLine);

        // Save updated AppliedPenalty
        AppliedPenalty savedAppliedPenalty = appliedPenaltyRepository.save(existingAppliedPenalty);

        return  savedAppliedPenalty;
    }
    

    public List<AppliedPenalty> findAll() {
        return appliedPenaltyRepository.findAll();
    }

    public Optional<AppliedPenalty> findById(Long id) {
        return appliedPenaltyRepository.findById(id);
    }

    public List<AppliedPenalty> findByRentLineId(Long rentLineId) {
        return appliedPenaltyRepository.findByRentLineId(rentLineId);
    }

    public List<AppliedPenalty> findByRentId(Long rentId) {
        return appliedPenaltyRepository.findByRentId(rentId);
    }

    public AppliedPenalty save(AppliedPenalty appliedPenalty) {
        validationService.validateEntity(appliedPenalty);
        return appliedPenaltyRepository.save(appliedPenalty);
    }

    public void deleteById(Long id) {
        Optional<AppliedPenalty> optionalPenalty = appliedPenaltyRepository.findById(id);

        if (optionalPenalty.isPresent()) {
            AppliedPenalty appliedPenalty = optionalPenalty.get();
            Penalty penalty = appliedPenalty.getPenalty();

            if (penalty != null) {
                RentLine rentLine = rentLineService.getRentLineById(appliedPenalty.getRentLine().getRentLineId())
                        .orElseThrow(() -> new IllegalArgumentException("RentLine does not exist."));

                double penaltyPrice = penalty.getPrice();
                rentLine.setPenaltiesAmount(rentLine.getPenaltiesAmount() - penaltyPrice);
                rentLineService.updateRentLine(rentLine);
            }

            appliedPenaltyRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("AppliedPenalty with ID " + id + " does not exist.");
        }
    }
}
