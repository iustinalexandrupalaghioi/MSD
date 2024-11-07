package com.msd.erp.infrastructure.repositories;

import com.msd.erp.domain.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {

    @Query("SELECT r FROM Rent r WHERE r.customer.relationid = :customerId")
    List<Rent> findByCustomerId(@Param("customerId") Long customerId);
}
