package com.msd.erp.infrastructure.repositories;

import com.msd.erp.domain.SalesOrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalesOrderLineRepository extends JpaRepository<SalesOrderLine, Long> {

    @Query("SELECT sol FROM SalesOrderLine sol WHERE sol.salesOrder.salesOrderId = :salesOrderId")
    List<SalesOrderLine> findBySalesOrderId(@Param("salesOrderId") Long salesOrderId);

    @Query("SELECT sol FROM SalesOrderLine sol WHERE sol.article.articleid = :articleId")
    List<SalesOrderLine> findByArticleId(@Param("articleId") Long articleId);
}
