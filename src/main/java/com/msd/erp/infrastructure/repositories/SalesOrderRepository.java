package com.msd.erp.infrastructure.repositories;

import com.msd.erp.domain.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    @Query("SELECT so FROM SalesOrder so WHERE so.customerId = :customerId")
    List<SalesOrder> findByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT so FROM SalesOrder so WHERE so.date BETWEEN :startDate AND :endDate")
    List<SalesOrder> findByDateRange(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
