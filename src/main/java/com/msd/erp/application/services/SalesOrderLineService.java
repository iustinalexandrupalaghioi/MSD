package com.msd.erp.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.msd.erp.application.computations.OrdersAmountsService;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.Article;
import com.msd.erp.domain.SalesOrder;
import com.msd.erp.domain.SalesOrderLine;
import com.msd.erp.domain.SalesOrderState;
import com.msd.erp.infrastructure.repositories.ArticleRepository;
import com.msd.erp.infrastructure.repositories.SalesOrderLineRepository;
import com.msd.erp.infrastructure.repositories.SalesOrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesOrderLineService {

    private final SalesOrderLineRepository salesOrderLineRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final ArticleRepository articleRepository;
    private final DomainValidationService validationService;
    private final OrdersAmountsService ordersAmountsService;

    @Autowired
    private  SalesOrderService salesOrderService;

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

   
    public SalesOrderLine updateSalesOrderLine(Long id, SalesOrderLine salesOrderLine) {
       SalesOrderLine existingSalesOrderLine = salesOrderLineRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sales order line not found"));
       
        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderLine.getSalesOrder().getSalesOrderId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sales order not found"));

        Article article = articleRepository.findById(salesOrderLine.getArticle().getArticleid())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));

        // Calculate old amounts
        Double oldLineAmount = existingSalesOrderLine.getTotalLineAmount();
        Double oldLineAmountWithVAT = existingSalesOrderLine.getTotalLineAmountWithVAT();

        // Update fields
        salesOrderLine.setSalesOrder(salesOrder);
        salesOrderLine.setArticle(article);
       

        // Update header totals
        salesOrderService.updateSalesHeaderTotals(
            salesOrder,
            oldLineAmount,
            salesOrderLine.getTotalLineAmount(),
            oldLineAmountWithVAT,
            salesOrderLine.getTotalLineAmountWithVAT()
        );

        salesOrderService.saveSalesOrder(salesOrder);
        validationService.validateEntity(salesOrderLine);
        return salesOrderLineRepository.save(salesOrderLine);
    }
    

    @Transactional
    public void deleteSalesOrderLine(Long id) {
        if (salesOrderLineRepository.existsById(id)) {
            salesOrderLineRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Sales order line with ID " + id + " does not exist.");
        }
    }

    public boolean deleteSalesOrderLineAndUpdateSalesOrder(Long salesOrderLine) {
        Optional<SalesOrderLine> optionalSalesOrderLine = salesOrderLineRepository.findById(salesOrderLine);
        if (optionalSalesOrderLine.isPresent()) {
            SalesOrderLine orderLine = optionalSalesOrderLine.get();
             SalesOrder salesOrder = orderLine.getSalesOrder();
            
            if (salesOrder.getState() == SalesOrderState.CONFIRMED || 
                salesOrder.getState() == SalesOrderState.SENT){
                throw new IllegalStateException("Cannot delete sales order lines from a sales order in the " + salesOrder.getState() + " state.");
            }

            salesOrderService.updateSalesHeaderTotals(
                    salesOrder,
                    orderLine.getTotalLineAmount() * -1,
                    orderLine.getTotalLineAmountWithVAT() * -1
                );

            salesOrderService.save(salesOrder);

            salesOrderLineRepository.delete(orderLine);

            return true;
        }
        return false;
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
