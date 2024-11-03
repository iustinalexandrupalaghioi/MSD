package com.msd.erp.application.services;

import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.Penalty;
import com.msd.erp.infrastructure.repositories.PenaltyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PenaltyService {

    private final PenaltyRepository penaltyRepository;
    private final DomainValidationService validationService;

    @Transactional
    public Penalty createPenalty(Penalty penalty) {
        validationService.validateEntity(penalty);
        return penaltyRepository.save(penalty);
    }

    public List<Penalty> getAllPenalties() {
        return penaltyRepository.findAll();
    }

    public Optional<Penalty> getPenaltyById(Long id) {
        return penaltyRepository.findById(id);
    }

    @Transactional
    public Optional<Penalty> updatePenalty(Long id, Penalty updatedPenalty) {
        return penaltyRepository.findById(id).map(existingPenalty -> {
            existingPenalty.setDescription(updatedPenalty.getDescription());
            existingPenalty.setPenaltytype(updatedPenalty.getPenaltytype());

            validationService.validateEntity(existingPenalty);
            return penaltyRepository.save(existingPenalty);
        });
    }

    @Transactional
    public void deletePenalty(Long id) {
        if (penaltyRepository.existsById(id)) {
            penaltyRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Penalty with ID " + id + " does not exist.");
        }
    }

    public boolean penaltyExists(Long id) {
        return penaltyRepository.existsById(id);
    }
}