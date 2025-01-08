package com.msd.erp.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.msd.erp.application.computations.OrdersAmountsService;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.Article;
import com.msd.erp.domain.SalesOrder;
import com.msd.erp.domain.SalesOrderLine;
import com.msd.erp.infrastructure.repositories.ArticleRepository;
import com.msd.erp.infrastructure.repositories.SalesOrderLineRepository;
import com.msd.erp.infrastructure.repositories.SalesOrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesOrderLineService {

    private final SalesOrderLineRepository salesOrderLineRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderService salesOrderService;
    private final ArticleRepository articleRepository;
    private final DomainValidationService validationService;
    private final OrdersAmountsService ordersAmountsService;
    public SalesOrderLine saveSalesOrderLine(SalesOrderLine salesOrderLine) {
        // Calculează totalurile folosind OrdersAmountsService
        Double lineAmount = ordersAmountsService.calculateSalesLineAmount(salesOrderLine);
        Double lineAmountWithVAT = ordersAmountsService.calculateSalesLineAmountWithVAT(salesOrderLine);

        // Setează totalurile calculate în obiectul salesOrderLine
        salesOrderLine.setTotalLineAmount(lineAmount);
        salesOrderLine.setTotalLineAmountWithVAT(lineAmountWithVAT);

        // Salvează salesOrderLine în baza de date
        return salesOrderLineRepository.save(salesOrderLine);
    }
    @Transactional
    public SalesOrderLine createSalesOrderLine(SalesOrderLine salesOrderLine) {
        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderLine.getSalesOrder().getSalesOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sales order not found"));

        Article article = articleRepository.findById(salesOrderLine.getArticle().getArticleid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));

        salesOrderLine.setSalesOrder(salesOrder);
        salesOrderLine.setArticle(article);
        validationService.validateEntity(salesOrderLine);
        

        salesOrderService.updateSalesHeaderTotals(salesOrder, salesOrderLine.getTotalLineAmount(), salesOrderLine.getTotalLineAmountWithVAT());

        salesOrderService.saveSalesOrder(salesOrder);
        return salesOrderLineRepository.save(salesOrderLine);
    }

    public List<SalesOrderLine> getAllSalesOrderLines() {
        return salesOrderLineRepository.findAll();
    }

    public Optional<SalesOrderLine> getSalesOrderLineById(Long id) {
        return salesOrderLineRepository.findById(id);
    }

    @Transactional
    public Optional<SalesOrderLine> updateSalesOrderLine(Long id, SalesOrderLine updatedSalesOrderLine) {
       SalesOrderLine existingSalesOrderLine = salesOrderLineRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sales order line not found"));

        SalesOrder salesOrder = salesOrderRepository.findById(updatedSalesOrderLine.getSalesOrder().getSalesOrderId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sales order not found"));

        Article article = articleRepository.findById(updatedSalesOrderLine.getArticle().getArticleid())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));

        // Calculate old amounts
        Double oldLineAmount = existingSalesOrderLine.getTotalLineAmount();
        Double oldLineAmountWithVAT = existingSalesOrderLine.getTotalLineAmountWithVAT();

        // Update fields
        updatedSalesOrderLine.setSalesOrder(salesOrder);
        updatedSalesOrderLine.setArticle(article);
        validationService.validateEntity(updatedSalesOrderLine);

        // Update header totals
        salesOrderService.updateSalesHeaderTotals(
            salesOrder,
            oldLineAmount,
            updatedSalesOrderLine.getTotalLineAmount(),
            oldLineAmountWithVAT,
            updatedSalesOrderLine.getTotalLineAmountWithVAT()
        );

        salesOrderService.saveSalesOrder(salesOrder);

        SalesOrderLine savedSalesOrderLine = salesOrderLineRepository.save(updatedSalesOrderLine);
        return Optional.of(savedSalesOrderLine);
    }
    

    @Transactional
    public void deleteSalesOrderLine(Long id) {
        if (salesOrderLineRepository.existsById(id)) {
            salesOrderLineRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Sales order line with ID " + id + " does not exist.");
        }
    }

    public boolean salesOrderLineExists(Long id) {
        return salesOrderLineRepository.existsById(id);
    }

    public List<SalesOrderLine> getSalesOrderLinesBySalesOrderId(Long salesOrderId) {
        return salesOrderLineRepository.findBySalesOrderId(salesOrderId);
    }

    public List<SalesOrderLine> getSalesOrderLinesByArticleId(Long articleId) {
        return salesOrderLineRepository.findByArticleId(articleId);
    }
}
