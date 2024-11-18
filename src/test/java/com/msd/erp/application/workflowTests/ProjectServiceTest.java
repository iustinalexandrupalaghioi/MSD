package com.msd.erp.application.workflowTests;

import com.msd.erp.application.services.ProjectService;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.Project;
import com.msd.erp.domain.Relation;
import com.msd.erp.domain.ProjectType;
import com.msd.erp.infrastructure.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        project = new Project();
        project.setProjectId(1L);
        project.setCustomerId(customer);
        project.setStartDate(LocalDateTime.of(2024, 1, 1, 9, 0));
        project.setEndDate(LocalDateTime.of(2024, 12, 31, 18, 0));
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
        updatedProject.setStartDate(LocalDateTime.of(2025, 1, 1, 9, 0));
        updatedProject.setEndDate(LocalDateTime.of(2025, 12, 31, 18, 0));
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
