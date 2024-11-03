package com.msd.erp.infrastructure.repositories;

import com.msd.erp.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p WHERE p.customerId.relationid= :customerId")
    List<Project> findByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT p FROM Project p WHERE p.projectType = :projectType")
    List<Project> findByProjectType(@Param("projectType") String projectType);
}
