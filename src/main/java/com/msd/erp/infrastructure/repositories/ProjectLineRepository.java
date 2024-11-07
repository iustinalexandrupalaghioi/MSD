package com.msd.erp.infrastructure.repositories;

import com.msd.erp.domain.ProjectLine;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectLineRepository extends JpaRepository<ProjectLine, Long> {

    @Query("SELECT pl FROM ProjectLine pl WHERE pl.project.projectId = :projectId")
    List<ProjectLine> findByProjectId(@Param("projectId") Long projectId);
}
