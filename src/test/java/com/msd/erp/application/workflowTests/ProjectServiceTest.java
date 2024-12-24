package com.msd.erp.application.workflowTests;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.msd.erp.application.services.ProjectService;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.Project;
import com.msd.erp.domain.ProjectType;
import com.msd.erp.domain.Relation;
import com.msd.erp.infrastructure.repositories.ProjectRepository;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private DomainValidationService validationService;

    @InjectMocks
    private ProjectService projectService;

    private Project project;
    private Relation customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customer = new Relation();
        customer.setRelationid(1L);
        customer.setName("Test Customer");

        LocalDate startDate = LocalDate.of(2024, 12, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        project = new Project();
        project.setProjectId(1L);
        project.setCustomerId(customer);
        project.setStartDate(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        project.setEndDate(Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        project.setProjectType(ProjectType.RESIDENTIAL);
        project.setBudget(50000.0);
        project.setIsInBudget(true);
    }

    @Test
    void createProject_ShouldSaveProject() {
        when(projectRepository.save(project)).thenReturn(project);

        Project result = projectService.createProject(project);

        assertNotNull(result);
        assertEquals(project.getProjectId(), result.getProjectId());
        verify(validationService).validateEntity(project);
        verify(projectRepository).save(project);
    }

    @Test
    void getProjectById_ShouldReturnProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Optional<Project> result = projectService.getProjectById(1L);

        assertTrue(result.isPresent());
        assertEquals(project.getProjectId(), result.get().getProjectId());
        verify(projectRepository).findById(1L);
    }

    @Test
    void updateProject_ShouldUpdateExistingProject() {
        Project updatedProject = new Project();

         LocalDate startDate = LocalDate.of(2024, 12, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        updatedProject.setStartDate(Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        updatedProject.setEndDate(Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        updatedProject.setProjectType(ProjectType.COMERCIAL);
        updatedProject.setBudget(60000.0);
        updatedProject.setIsInBudget(false);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);

        Optional<Project> result = projectService.updateProject(1L, updatedProject);

        assertTrue(result.isPresent());
        assertEquals(LocalDateTime.of(2025, 1, 1, 9, 0), result.get().getStartDate());
        assertEquals(LocalDateTime.of(2025, 12, 31, 18, 0), result.get().getEndDate());
        assertEquals(ProjectType.COMERCIAL, result.get().getProjectType());
        assertEquals(60000.0, result.get().getBudget());
        assertFalse(result.get().getIsInBudget());
        verify(validationService).validateEntity(project);
        verify(projectRepository).save(project);
    }

    @Test
    void deleteProject_ShouldDeleteProject() {
        when(projectRepository.existsById(1L)).thenReturn(true);

        projectService.deleteProject(1L);

        verify(projectRepository).deleteById(1L);
    }

    @Test
    void deleteProject_ShouldThrowExceptionIfNotFound() {
        when(projectRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> projectService.deleteProject(1L));

        assertEquals("Project with ID 1 does not exist.", exception.getMessage());
    }
}
