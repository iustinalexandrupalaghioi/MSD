package com.msd.erp.application.services;

import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.Project;
import com.msd.erp.infrastructure.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final DomainValidationService validationService;

    @Transactional
    public Project createProject(Project project) {
        validationService.validateEntity(project);
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    @Transactional
    public Optional<Project> updateProject(Long id, Project updatedProject) {
        return projectRepository.findById(id).map(existingProject -> {
            existingProject.setCustomerId(updatedProject.getCustomerId());
            existingProject.setStartDate(updatedProject.getStartDate());
            existingProject.setEndDate(updatedProject.getEndDate());
            existingProject.setProjectType(updatedProject.getProjectType());
            existingProject.setBudget(updatedProject.getBudget());
            existingProject.setIsInBudget(updatedProject.getIsInBudget());

            validationService.validateEntity(existingProject);
            return projectRepository.save(existingProject);
        });
    }

    @Transactional
    public void deleteProject(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Project with ID " + id + " does not exist.");
        }
    }

    public boolean projectExists(Long id) {
        return projectRepository.existsById(id);
    }

    public List<Project> getProjectsByCustomerId(Long customerId) {
        return projectRepository.findByCustomerId(customerId);
    }

    public List<Project> getProjectsByType(String projectType) {
        return projectRepository.findByProjectType(projectType);
    }
}
