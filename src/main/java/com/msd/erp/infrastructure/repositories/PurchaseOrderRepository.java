package com.msd.erp.infrastructure.repositories;

import com.msd.erp.domain.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    @Query("SELECT po FROM PurchaseOrder po WHERE po.supplierId.relationid = :supplierId")
    List<PurchaseOrder> findBySupplierId(@Param("supplierId") Long supplierId);

    
}
