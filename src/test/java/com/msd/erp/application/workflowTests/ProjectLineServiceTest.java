package com.msd.erp.application.workflowTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.msd.erp.application.computations.ProjectLineComputation;
import com.msd.erp.application.services.ProjectLineService;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.application.views.ProjectLineDTO;
import com.msd.erp.domain.Article;
import com.msd.erp.domain.ProjectLine;
import com.msd.erp.domain.PurchaseOrder;
import com.msd.erp.infrastructure.repositories.ProjectLineRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProjectLineServiceTest {

    @Mock
    private ProjectLineRepository projectLineRepository;

    @Mock
    private DomainValidationService validationService;

    @InjectMocks
    private ProjectLineService projectLineService;

    private ProjectLine mockProjectLine;
    private ProjectLineDTO projectLineDTO;
    private Article mockArticle;
    private PurchaseOrder mockPurchaseOrder;

    @BeforeEach
    void setUp() {
        mockProjectLine = new ProjectLine();
        mockProjectLine.setProjectLineId(1L);
        mockProjectLine.setQuantity(10);
        mockProjectLine.setPrice(100.0);

        mockArticle = new Article();
        mockArticle.setArticleid(1L);

        mockPurchaseOrder = new PurchaseOrder();
        mockPurchaseOrder.setPurchaseOrderId(1L);

        projectLineDTO = new ProjectLineDTO();
        projectLineDTO.setArticle(mockArticle);
        projectLineDTO.setPurchaseOrder(mockPurchaseOrder);
        projectLineDTO.setQuantity(20);
        projectLineDTO.setPrice(150.0);
    }

    @Test
    void create_ShouldSaveAndReturnProjectLine() {
        when(projectLineRepository.save(any(ProjectLine.class))).thenReturn(mockProjectLine);

        ProjectLine savedProjectLine = projectLineService.save(mockProjectLine);

        assertNotNull(savedProjectLine);
        assertEquals(mockProjectLine.getProjectLineId(), savedProjectLine.getProjectLineId());
        verify(projectLineRepository, times(1)).save(mockProjectLine);
    }

    @Test
    void readById_ShouldReturnProjectLineIfExists() {
        when(projectLineRepository.findById(1L)).thenReturn(Optional.of(mockProjectLine));

        Optional<ProjectLine> foundProjectLine = projectLineService.findById(1L);

        assertTrue(foundProjectLine.isPresent());
        assertEquals(mockProjectLine.getProjectLineId(), foundProjectLine.get().getProjectLineId());
        verify(projectLineRepository, times(1)).findById(1L);
    }

    @Test
    void readById_ShouldReturnEmptyIfNotExists() {
        when(projectLineRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<ProjectLine> foundProjectLine = projectLineService.findById(2L);

        assertFalse(foundProjectLine.isPresent());
        verify(projectLineRepository, times(1)).findById(2L);
    }

    @Test
    void readAll_ShouldReturnListOfProjectLines() {
        List<ProjectLine> projectLines = List.of(mockProjectLine, new ProjectLine());
        when(projectLineRepository.findAll()).thenReturn(projectLines);

        List<ProjectLine> foundProjectLines = projectLineService.findAll();

        assertNotNull(foundProjectLines);
        assertEquals(2, foundProjectLines.size());
        verify(projectLineRepository, times(1)).findAll();
    }

    @Test
    void updateProjectLine_ShouldUpdateAndReturnUpdatedProjectLine() {
        when(projectLineRepository.save(any(ProjectLine.class))).thenReturn(mockProjectLine);

        mockProjectLine.setPrice(100.0);
        mockProjectLine.setQuantity(10);

        ProjectLine updatedProjectLine = projectLineService.updateProjectLine(mockProjectLine, projectLineDTO);

        assertNotNull(updatedProjectLine);
        assertEquals(mockArticle, updatedProjectLine.getArticle());
        assertEquals(mockPurchaseOrder, updatedProjectLine.getPurchaseOrder());
        assertEquals(20, updatedProjectLine.getQuantity());
        assertEquals(150.0, updatedProjectLine.getPrice());
        assertEquals(ProjectLineComputation.calculateLineAmount(20, 150.0), updatedProjectLine.getLineAmount());
        verify(projectLineRepository, times(1)).save(updatedProjectLine);
    }

    @Test
    void updateProjectLine_ShouldNotUpdateIfNoChangesInDTO() {
        ProjectLineDTO emptyDTO = new ProjectLineDTO();
        when(projectLineRepository.save(any(ProjectLine.class))).thenReturn(mockProjectLine);

        ProjectLine updatedProjectLine = projectLineService.updateProjectLine(mockProjectLine, emptyDTO);

        assertNotNull(updatedProjectLine);
        assertEquals(mockProjectLine, updatedProjectLine);
        verify(projectLineRepository, times(1)).save(mockProjectLine);
    }

    @Test
    void updateProjectLine_ShouldRecalculateLineAmountWhenPriceChanges() {
        projectLineDTO.setPrice(200.0);
        when(projectLineRepository.save(any(ProjectLine.class))).thenReturn(mockProjectLine);

        double expectedLineAmount = ProjectLineComputation.calculateLineAmount(20, 200.0);

        ProjectLine updatedProjectLine = projectLineService.updateProjectLine(mockProjectLine, projectLineDTO);

        assertNotNull(updatedProjectLine);
        assertEquals(200.0, updatedProjectLine.getPrice());
        assertEquals(expectedLineAmount, updatedProjectLine.getLineAmount());
        verify(projectLineRepository, times(1)).save(updatedProjectLine);
    }

    @Test
    void deleteById_ShouldDeleteProjectLine() {
        doNothing().when(projectLineRepository).deleteById(1L);

        projectLineService.deleteById(1L);

        verify(projectLineRepository, times(1)).deleteById(1L);
    }

}
