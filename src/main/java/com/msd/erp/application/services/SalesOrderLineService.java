package com.msd.erp.application.services;

import com.msd.erp.application.computations.OrdersAmountsService;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.SalesOrderLine;
import com.msd.erp.infrastructure.repositories.SalesOrderLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalesOrderLineService {

    private final SalesOrderLineRepository salesOrderLineRepository;
    private final DomainValidationService validationService;
    private final OrdersAmountsService ordersAmountsService;
    public SalesOrderLine saveSalesOrderLine(SalesOrderLine salesOrderLine) {
        // Calculează totalurile folosind OrdersAmountsService
        BigDecimal lineAmount = ordersAmountsService.calculateSalesLineAmount(salesOrderLine);
        BigDecimal lineAmountWithVAT = ordersAmountsService.calculateSalesLineAmountWithVAT(salesOrderLine);

        // Setează totalurile calculate în obiectul salesOrderLine
        salesOrderLine.setTotalLineAmount(lineAmount);
        salesOrderLine.setTotalLineAmountWithVAT(lineAmountWithVAT);

        // Salvează salesOrderLine în baza de date
        return salesOrderLineRepository.save(salesOrderLine);
    }
    @Transactional
    public SalesOrderLine createSalesOrderLine(SalesOrderLine salesOrderLine) {
        validationService.validateEntity(salesOrderLine);
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
        return salesOrderLineRepository.findById(id).map(existingSalesOrderLine -> {
            existingSalesOrderLine.setSalesOrder(updatedSalesOrderLine.getSalesOrder());
            existingSalesOrderLine.setArticle(updatedSalesOrderLine.getArticle());
            existingSalesOrderLine.setQuantity(updatedSalesOrderLine.getQuantity());
            existingSalesOrderLine.setTotalLineAmount(updatedSalesOrderLine.getTotalLineAmount());
            existingSalesOrderLine.setTotalLineAmountWithVAT(updatedSalesOrderLine.getTotalLineAmountWithVAT());
            existingSalesOrderLine.setPrice(updatedSalesOrderLine.getPrice());

            validationService.validateEntity(existingSalesOrderLine);
            return salesOrderLineRepository.save(existingSalesOrderLine);
        });
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
