package com.msd.erp.application.workflowTests;

import com.msd.erp.application.services.PenaltyService;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.Penalty;
import com.msd.erp.domain.PenaltyType;
import com.msd.erp.infrastructure.repositories.PenaltyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PenaltyServiceTest {

    @Mock
    private PenaltyRepository penaltyRepository;

    @Mock
    private DomainValidationService validationService;

    @InjectMocks
    private PenaltyService penaltyService;

    private Penalty penalty;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        penalty = new Penalty();
        penalty.setPenaltyid(1L);
        penalty.setDescription("Late payment penalty");
        penalty.setPenaltytype(PenaltyType.LATE_PAYMENT);
        penalty.setPrice(50.0);
    }

    @Test
    void createPenalty_ShouldSavePenalty() {
        when(penaltyRepository.save(penalty)).thenReturn(penalty);

        Penalty result = penaltyService.createPenalty(penalty);

        assertNotNull(result);
        assertEquals(penalty.getPenaltyid(), result.getPenaltyid());
        verify(validationService).validateEntity(penalty);
        verify(penaltyRepository).save(penalty);
    }

    @Test
    void getPenaltyById_ShouldReturnPenalty() {
        when(penaltyRepository.findById(1L)).thenReturn(Optional.of(penalty));

        Optional<Penalty> result = penaltyService.getPenaltyById(1L);

        assertTrue(result.isPresent());
        assertEquals(penalty.getPenaltyid(), result.get().getPenaltyid());
        verify(penaltyRepository).findById(1L);
    }

    @Test
    void updatePenalty_ShouldUpdateExistingPenalty() {
        Penalty updatedPenalty = new Penalty();
        updatedPenalty.setDescription("Late return penalty");
        updatedPenalty.setPenaltytype(PenaltyType.LATE_RETURN);
        updatedPenalty.setPrice(75.0);

        when(penaltyRepository.findById(1L)).thenReturn(Optional.of(penalty));
        when(penaltyRepository.save(penalty)).thenReturn(penalty);

        Optional<Penalty> result = penaltyService.updatePenalty(1L, updatedPenalty);

        assertTrue(result.isPresent());
        assertEquals("Late return penalty", result.get().getDescription());
        assertEquals(PenaltyType.LATE_RETURN, result.get().getPenaltytype());
        assertEquals(75.0, result.get().getPrice());
        verify(validationService).validateEntity(penalty);
        verify(penaltyRepository).save(penalty);
    }

    @Test
    void deletePenalty_ShouldDeletePenalty() {
        when(penaltyRepository.existsById(1L)).thenReturn(true);

        penaltyService.deletePenalty(1L);

        verify(penaltyRepository).deleteById(1L);
    }

    @Test
    void deletePenalty_ShouldThrowExceptionIfNotFound() {
        when(penaltyRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> penaltyService.deletePenalty(1L));

        assertEquals("Penalty with ID 1 does not exist.", exception.getMessage());
    }
}
