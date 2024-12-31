package com.msd.erp.infrastructure.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.msd.erp.domain.AppliedPenalty;

@Repository
public interface AppliedPenaltyRepository extends JpaRepository<AppliedPenalty, Long> {

    @Query("SELECT ap FROM AppliedPenalty ap WHERE ap.rentLine.rentLineId = :rentLineId")
    List<AppliedPenalty> findByRentLineId(@Param("rentLineId") Long rentLineId);

    @Query("SELECT ap FROM AppliedPenalty ap WHERE ap.rentLine.rent.rentId = :rentId")
    List<AppliedPenalty> findByRentId(@Param("rentId") Long rentId);
}
