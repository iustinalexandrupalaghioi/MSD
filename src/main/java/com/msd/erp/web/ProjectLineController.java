package com.msd.erp.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msd.erp.application.computations.ProjectLineComputation;
import com.msd.erp.application.services.ProjectLineService;
import com.msd.erp.application.services.ProjectService;
import com.msd.erp.application.views.ProjectLineDTO;
import com.msd.erp.domain.Project;
import com.msd.erp.domain.ProjectLine;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/project-lines")
public class ProjectLineController {

    private final ProjectLineService projectLineService;

    @Autowired
    private ProjectService projectService;

    public ProjectLineController(ProjectLineService projectLineService) {
        this.projectLineService = projectLineService;
    }

    // Create or update a ProjectLine
    @PostMapping("/create")
    public ResponseEntity<ProjectLine> createOrUpdateProjectLine(@RequestBody ProjectLine projectLine) {
        projectLine.setLineAmount(ProjectLineComputation
                .calculateLineAmount(projectLine.getQuantity(), projectLine.getPrice()));

        Optional<Project> optionalProject = projectService.getProjectById(projectLine.getProject().getProjectId());

        if (optionalProject.isPresent()) {
            optionalProject.get()
                    .setIsInBudget(projectLine.getLineAmount() <= optionalProject.get().getBudget().doubleValue());
        }

        ProjectLine savedProjectLine = projectLineService.save(projectLine);
        return ResponseEntity.ok(savedProjectLine);
    }

    // Get all ProjectLines
    @GetMapping
    public ResponseEntity<List<ProjectLine>> getAllProjectLines() {
        List<ProjectLine> projectLines = projectLineService.findAll();
        return ResponseEntity.ok(projectLines);
    }

    // Get ProjectLine by ID
    @GetMapping("/{projectLineId}")
    public ResponseEntity<ProjectLine> getProjectLineById(@PathVariable Long projectLineId) {
        Optional<ProjectLine> projectLine = projectLineService.findById(projectLineId);
        return projectLine.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a ProjectLine
    @PutMapping("/update/{projectLineId}")
    public ResponseEntity<ProjectLine> updateProjectLine(
            @PathVariable Long projectLineId,
            @RequestBody @Valid ProjectLineDTO projectLineDTO) {

        try {
            Optional<ProjectLine> existingProjectLineOpt = projectLineService.findById(projectLineId);

            if (existingProjectLineOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            ProjectLine updatedProjectLine = projectLineService.updateProjectLine(existingProjectLineOpt.get(),
                    projectLineDTO);
            return ResponseEntity.ok(updatedProjectLine);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete a ProjectLine
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProjectLine(@PathVariable Long id) {

        projectLineService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
