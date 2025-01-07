package com.msd.erp.application.workflowTests;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.msd.erp.application.services.AppliedPenaltyService;
import com.msd.erp.application.services.ArticleService;
import com.msd.erp.application.services.PenaltyService;
import com.msd.erp.application.services.RentLineService;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.AppliedPenalty;
import com.msd.erp.domain.Article;
import com.msd.erp.domain.Penalty;
import com.msd.erp.domain.RentLine;
import com.msd.erp.infrastructure.repositories.AppliedPenaltyRepository;

@ExtendWith(MockitoExtension.class)
class AppliedPenaltyServiceTest {

    @Mock
    private AppliedPenaltyRepository appliedPenaltyRepository;

    @Mock
    private RentLineService rentLineService;

    @Mock
    private PenaltyService penaltyService;

    @Mock
    private ArticleService articleService;

    @Mock
    private DomainValidationService validationService;

    @InjectMocks
    private AppliedPenaltyService appliedPenaltyService;

    private AppliedPenalty mockAppliedPenalty;
    private RentLine mockRentLine;
    private Penalty mockPenalty;
    private Article mockArticle;

    @BeforeEach
    void setUp() {
        mockRentLine = new RentLine();
        mockRentLine.setRentLineId(1L);
        mockRentLine.setPenaltiesAmount(0.0);

        mockPenalty = new Penalty();
        mockPenalty.setPenaltyid(1L);
        mockPenalty.setPrice(50.0);

        mockArticle = new Article();
        mockArticle.setArticleid(1L);

        mockAppliedPenalty = new AppliedPenalty();
        mockAppliedPenalty.setRentLine(mockRentLine);
        mockAppliedPenalty.setPenalty(mockPenalty);
    }

   @Test
void createPenalty_ShouldSaveAndReturnAppliedPenalty() {
    when(rentLineService.getRentLineById(1L)).thenReturn(Optional.of(mockRentLine));
    when(penaltyService.getPenaltyById(1L)).thenReturn(Optional.of(mockPenalty));
    when(appliedPenaltyRepository.save(any(AppliedPenalty.class))).thenReturn(mockAppliedPenalty);

    AppliedPenalty savedAppliedPenalty = appliedPenaltyService.createPenalty(mockAppliedPenalty);

    assertNotNull(savedAppliedPenalty);
    assertEquals(mockAppliedPenalty.getRentLine(), savedAppliedPenalty.getRentLine());
    assertEquals(mockAppliedPenalty.getPenalty(), savedAppliedPenalty.getPenalty());
    verify(appliedPenaltyRepository, times(1)).save(mockAppliedPenalty);

    verify(rentLineService, times(1)).getRentLineById(1L);
    verify(penaltyService, times(1)).getPenaltyById(1L);
}


   @Test
void createPenalty_ShouldThrowExceptionIfRentLineDoesNotExist() {
    when(rentLineService.getRentLineById(1L)).thenReturn(Optional.empty());

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
        appliedPenaltyService.createPenalty(mockAppliedPenalty);
    });

    assertEquals("RentLine with ID 1 does not exist.", thrown.getMessage());
    verify(rentLineService, times(1)).getRentLineById(1L);
    verifyNoInteractions(penaltyService);
    verifyNoInteractions(articleService);
}


    @Test
    void createPenalty_ShouldThrowExceptionIfPenaltyDoesNotExist() {
        when(rentLineService.getRentLineById(1L)).thenReturn(Optional.of(mockRentLine));
        when(penaltyService.getPenaltyById(1L)).thenReturn(Optional.empty()); 

       
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            appliedPenaltyService.createPenalty(mockAppliedPenalty);
        });

        assertEquals("Penalty with ID 1 does not exist.", thrown.getMessage());

    
        verify(rentLineService, times(1)).getRentLineById(1L);
        verify(penaltyService, times(1)).getPenaltyById(1L);
        verifyNoInteractions(articleService);
    }

    
    @Test
    void findAll_ShouldReturnListOfAppliedPenalties() {
        List<AppliedPenalty> appliedPenalties = List.of(mockAppliedPenalty, new AppliedPenalty());
        when(appliedPenaltyRepository.findAll()).thenReturn(appliedPenalties);

        List<AppliedPenalty> foundAppliedPenalties = appliedPenaltyService.findAll();

        assertNotNull(foundAppliedPenalties);
        assertEquals(2, foundAppliedPenalties.size());
        verify(appliedPenaltyRepository, times(1)).findAll();
    }

    @Test
    void findById_ShouldReturnAppliedPenaltyIfExists() {
        when(appliedPenaltyRepository.findById(1L)).thenReturn(Optional.of(mockAppliedPenalty));

        Optional<AppliedPenalty> foundAppliedPenalty = appliedPenaltyService.findById(1L);

        assertTrue(foundAppliedPenalty.isPresent());
        assertEquals(mockAppliedPenalty.getRentLine(), foundAppliedPenalty.get().getRentLine());
        verify(appliedPenaltyRepository, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldReturnEmptyIfNotExists() {
        when(appliedPenaltyRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<AppliedPenalty> foundAppliedPenalty = appliedPenaltyService.findById(2L);

        assertFalse(foundAppliedPenalty.isPresent());
        verify(appliedPenaltyRepository, times(1)).findById(2L);
    }

    @Test
    void deleteById_ShouldDeleteAppliedPenalty() {
        when(appliedPenaltyRepository.findById(1L)).thenReturn(Optional.of(mockAppliedPenalty));
        when(rentLineService.getRentLineById(1L)).thenReturn(Optional.of(mockRentLine));

        doNothing().when(appliedPenaltyRepository).deleteById(1L);

        appliedPenaltyService.deleteById(1L);

        verify(appliedPenaltyRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_ShouldThrowExceptionIfNotExists() {
        when(appliedPenaltyRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            appliedPenaltyService.deleteById(1L);
        });

        assertEquals("AppliedPenalty with ID 1 does not exist.", thrown.getMessage());
    }
}
