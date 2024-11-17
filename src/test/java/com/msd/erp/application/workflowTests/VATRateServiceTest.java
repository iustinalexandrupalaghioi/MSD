package com.msd.erp.application.workflowTests;

import com.msd.erp.application.services.VATRateService;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.VATRate;
import com.msd.erp.infrastructure.repositories.VATRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VATRateServiceTest {

    private VATRateService vatRateService;
    private VATRateRepository vatRateRepository;
    private DomainValidationService validationService;

    @BeforeEach
    void setUp() {
        vatRateRepository = mock(VATRateRepository.class);
        validationService = mock(DomainValidationService.class);
        vatRateService = new VATRateService(vatRateRepository, validationService);
    }

    @Test
    void createVATRate_ShouldSaveAndReturnVATRate() {
        VATRate vatRate = new VATRate(null, 19.0);
        VATRate savedVATRate = new VATRate(1L, 19.0);

        when(vatRateRepository.save(vatRate)).thenReturn(savedVATRate);

        VATRate result = vatRateService.createVATRate(vatRate);

        verify(validationService).validateEntity(vatRate); // Validare apelată
        verify(vatRateRepository).save(vatRate); // Save apelat
        assertEquals(savedVATRate, result); // Verificăm rezultatul
    }

    @Test
    void getAllVATRates_ShouldReturnListOfVATRates() {
        VATRate vatRate1 = new VATRate(1L, 19.0);
        VATRate vatRate2 = new VATRate(2L, 5.0);

        when(vatRateRepository.findAll()).thenReturn(List.of(vatRate1, vatRate2));

        List<VATRate> result = vatRateService.getAllVATRates();

        verify(vatRateRepository).findAll();
        assertEquals(2, result.size());
        assertTrue(result.contains(vatRate1));
        assertTrue(result.contains(vatRate2));
    }

    @Test
    void getVATRateById_ShouldReturnVATRateIfFound() {
        VATRate vatRate = new VATRate(1L, 19.0);

        when(vatRateRepository.findById(1L)).thenReturn(Optional.of(vatRate));

        Optional<VATRate> result = vatRateService.getVATRateById(1L);

        verify(vatRateRepository).findById(1L);
        assertTrue(result.isPresent());
        assertEquals(vatRate, result.get());
    }

    @Test
    void getVATRateById_ShouldReturnEmptyOptionalIfNotFound() {
        when(vatRateRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<VATRate> result = vatRateService.getVATRateById(1L);

        verify(vatRateRepository).findById(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void updateVATRate_ShouldUpdateAndReturnUpdatedVATRate() {
        VATRate existingVATRate = new VATRate(1L, 19.0);
        VATRate updatedVATRate = new VATRate(1L, 9.0);

        when(vatRateRepository.findById(1L)).thenReturn(Optional.of(existingVATRate));
        when(vatRateRepository.save(existingVATRate)).thenReturn(updatedVATRate);

        Optional<VATRate> result = vatRateService.updateVATRate(1L, updatedVATRate);

        assertTrue(result.isPresent());
        assertEquals(9.0, result.get().getPercent());

        verify(vatRateRepository).findById(1L);
        verify(vatRateRepository).save(existingVATRate);

        ArgumentCaptor<VATRate> captor = ArgumentCaptor.forClass(VATRate.class);
        verify(vatRateRepository).save(captor.capture());
        assertEquals(9.0, captor.getValue().getPercent());
    }

    @Test
    void deleteVATRate_ShouldDeleteIfExists() {
        when(vatRateRepository.existsById(1L)).thenReturn(true);

        vatRateService.deleteVATRate(1L);

        verify(vatRateRepository).existsById(1L);
        verify(vatRateRepository).deleteById(1L);
    }

    @Test
    void deleteVATRate_ShouldThrowExceptionIfNotExists() {
        when(vatRateRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            vatRateService.deleteVATRate(1L);
        });

        assertEquals("VATRate with ID 1 does not exist.", exception.getMessage());
        verify(vatRateRepository).existsById(1L);
        verify(vatRateRepository, never()).deleteById(1L);
    }

    @Test
    void vatRateExists_ShouldReturnTrueIfExists() {
        when(vatRateRepository.existsById(1L)).thenReturn(true);

        boolean exists = vatRateService.vatRateExists(1L);

        verify(vatRateRepository).existsById(1L);
        assertTrue(exists);
    }

    @Test
    void vatRateExists_ShouldReturnFalseIfNotExists() {
        when(vatRateRepository.existsById(1L)).thenReturn(false);

        boolean exists = vatRateService.vatRateExists(1L);

        verify(vatRateRepository).existsById(1L);
        assertFalse(exists);
    }
}
