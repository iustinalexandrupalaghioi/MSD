package com.msd.erp.application.services;

import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.ProjectLine;
import com.msd.erp.infrastructure.repositories.ProjectLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectLineService {

    private final ProjectLineRepository projectLineRepository;
    @Autowired
    private DomainValidationService validationService;

    @Autowired
    public ProjectLineService(ProjectLineRepository projectLineRepository) {
        this.projectLineRepository = projectLineRepository;
    }

    // Create or update a ProjectLine
    public ProjectLine save(ProjectLine projectLine) {
        validationService.validateEntity(projectLine);
        return projectLineRepository.save(projectLine);
    }

    // Get all ProjectLines
    public List<ProjectLine> findAll() {
        return projectLineRepository.findAll();
    }

    // Find a ProjectLine by ID
    public Optional<ProjectLine> findById(Long projectLineId) {
        return projectLineRepository.findById(projectLineId);
    }

    // Find ProjectLines by project ID
    public List<ProjectLine> findByProjectId(Long projectId) {
        return projectLineRepository.findByProjectId(projectId);
    }

    // Delete a ProjectLine by ID
    public void deleteById(Long projectLineId) {
        projectLineRepository.deleteById(projectLineId);
    }
}
