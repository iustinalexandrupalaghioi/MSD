package com.msd.erp.infrastructure.repositories;

import com.msd.erp.domain.RentLine;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RentLineRepository extends JpaRepository<RentLine, Long> {

    @Query("SELECT rl FROM RentLine rl WHERE rl.rent.rentId = :rentId")
    List<RentLine> findByRentId(@Param("rentId") Long rentId);

}
