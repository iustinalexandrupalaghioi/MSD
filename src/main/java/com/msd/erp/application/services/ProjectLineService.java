package com.msd.erp.application.services;

import com.msd.erp.application.computations.ProjectLineComputation;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.application.views.ProjectLineDTO;
import com.msd.erp.domain.ProjectLine;
import com.msd.erp.infrastructure.repositories.ProjectLineRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectLineService {
    @Autowired
    private ProjectLineRepository projectLineRepository;
    @Autowired
    private DomainValidationService validationService;

    // Create or update a ProjectLine
    public ProjectLine save(ProjectLine projectLine) {
        validationService.validateEntity(projectLine);
        return projectLineRepository.save(projectLine);
    }

    @Transactional
    public ProjectLine updateProjectLine(ProjectLine existingProjectLine, ProjectLineDTO projectLineDTO) {
        if (projectLineDTO.getArticle() != null) {
            existingProjectLine.setArticle(projectLineDTO.getArticle());
        }

        if (projectLineDTO.getPurchaseOrder() != null) {
            existingProjectLine.setPurchaseOrder(projectLineDTO.getPurchaseOrder());
        }

        if (projectLineDTO.getPurchaseOrderLine() != null) {
            existingProjectLine.setPurchaseOrderLine(projectLineDTO.getPurchaseOrderLine());
        }

        if (projectLineDTO.getQuantity() != null) {
            existingProjectLine.setQuantity(projectLineDTO.getQuantity());
        }

        if (projectLineDTO.getPrice() != null) {
            existingProjectLine.setPrice(projectLineDTO.getPrice());
        }

        // Update line amount
        existingProjectLine.setLineAmount(ProjectLineComputation
                .calculateLineAmount(existingProjectLine.getQuantity(), existingProjectLine.getPrice()));

        return save(existingProjectLine);
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
