package com.msd.erp.application.services;

import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.VATRate;
import com.msd.erp.infrastructure.repositories.VATRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VATRateService {

    private final VATRateRepository vatRateRepository;
    private final DomainValidationService validationService;

    @Transactional
    public VATRate createVATRate(VATRate vatRate) {
        validationService.validateEntity(vatRate);
        return vatRateRepository.save(vatRate);
    }

    public List<VATRate> getAllVATRates() {
        return vatRateRepository.findAll();
    }

    public Optional<VATRate> getVATRateById(Long id) {
        return vatRateRepository.findById(id);
    }

    @Transactional
    public Optional<VATRate> updateVATRate(Long id, VATRate updatedVATRate) {
        return vatRateRepository.findById(id).map(existingVATRate -> {
            existingVATRate.setPercent(updatedVATRate.getPercent());

            validationService.validateEntity(existingVATRate);
            return vatRateRepository.save(existingVATRate);
        });
    }

    @Transactional
    public void deleteVATRate(Long id) {
        if (vatRateRepository.existsById(id)) {
            vatRateRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("VATRate with ID " + id + " does not exist.");
        }
    }

    public boolean vatRateExists(Long id) {
        return vatRateRepository.existsById(id);
    }
}
