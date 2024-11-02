package com.msd.erp.infrastructure.repositories;

import com.msd.erp.domain.PurchaseOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseOrderLineRepository extends JpaRepository<PurchaseOrderLine, Long> {

    @Query("SELECT pol FROM PurchaseOrderLine pol WHERE pol.purchaseOrder.purchaseOrderId = :purchaseOrderId")
    List<PurchaseOrderLine> findByPurchaseOrderId(@Param("purchaseOrderId") Long purchaseOrderId);

    @Query("SELECT pol FROM PurchaseOrderLine pol WHERE pol.article.articleid = :articleId")
    List<PurchaseOrderLine> findByArticleId(@Param("articleId") Long articleId);


}
