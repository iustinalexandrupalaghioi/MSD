package com.msd.erp.infrastructure.repositories;

import com.msd.erp.domain.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    @Query("SELECT po FROM PurchaseOrder po WHERE po.customerId.relationid = :customerId")
    List<PurchaseOrder> findByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.projectId = :projectId")
    List<PurchaseOrder> findByProjectId(@Param("projectId") Long projectId);
}
