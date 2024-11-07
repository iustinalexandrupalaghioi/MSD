package com.msd.erp.application.services;

import com.msd.erp.application.computations.OrdersAmountsService;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.PurchaseOrderLine;
import com.msd.erp.infrastructure.repositories.PurchaseOrderLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseOrderLineService {

    private final PurchaseOrderLineRepository purchaseOrderLineRepository;
    private final DomainValidationService validationService;
    private final OrdersAmountsService ordersAmountsService;

    public PurchaseOrderLine savePurchaseOrderLine(PurchaseOrderLine purchaseOrderLine) {
        // Calculează totalurile folosind OrdersAmountsService
        Double lineAmount = ordersAmountsService.calculatePurchaseLineAmount(purchaseOrderLine);
        Double lineAmountWithVAT = ordersAmountsService.calculatePurchaseLineAmountWithVAT(purchaseOrderLine);

        purchaseOrderLine.setTotalLineAmount(lineAmount);
        purchaseOrderLine.setTotalLineAmountWithVAT(lineAmountWithVAT);

        return purchaseOrderLineRepository.save(purchaseOrderLine);
    }

    @Transactional
    public PurchaseOrderLine createPurchaseOrderLine(PurchaseOrderLine purchaseOrderLine) {
        validationService.validateEntity(purchaseOrderLine);
        return purchaseOrderLineRepository.save(purchaseOrderLine);
    }

    public List<PurchaseOrderLine> getAllPurchaseOrderLines() {
        return purchaseOrderLineRepository.findAll();
    }

    public Optional<PurchaseOrderLine> getPurchaseOrderLineById(Long id) {
        return purchaseOrderLineRepository.findById(id);
    }

    @Transactional
    public Optional<PurchaseOrderLine> updatePurchaseOrderLine(Long id, PurchaseOrderLine updatedPurchaseOrderLine) {
        return purchaseOrderLineRepository.findById(id).map(existingPurchaseOrderLine -> {
            existingPurchaseOrderLine.setPurchaseOrder(updatedPurchaseOrderLine.getPurchaseOrder());
            existingPurchaseOrderLine.setArticle(updatedPurchaseOrderLine.getArticle());
            existingPurchaseOrderLine.setQuantity(updatedPurchaseOrderLine.getQuantity());
            existingPurchaseOrderLine.setTotalLineAmount(updatedPurchaseOrderLine.getTotalLineAmount());
            existingPurchaseOrderLine.setTotalLineAmountWithVAT(updatedPurchaseOrderLine.getTotalLineAmountWithVAT());
            existingPurchaseOrderLine.setPrice(updatedPurchaseOrderLine.getPrice());

            validationService.validateEntity(existingPurchaseOrderLine);
            return purchaseOrderLineRepository.save(existingPurchaseOrderLine);
        });
    }

    @Transactional
    public void deletePurchaseOrderLine(Long id) {
        if (purchaseOrderLineRepository.existsById(id)) {
            purchaseOrderLineRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Purchase order line with ID " + id + " does not exist.");
        }
    }

    public boolean purchaseOrderLineExists(Long id) {
        return purchaseOrderLineRepository.existsById(id);
    }

    public List<PurchaseOrderLine> getPurchaseOrderLinesByPurchaseOrderId(Long purchaseOrderId) {
        return purchaseOrderLineRepository.findByPurchaseOrderId(purchaseOrderId);
    }

    public List<PurchaseOrderLine> getSalesOrderLinesByArticleId(Long articleId) {
        return purchaseOrderLineRepository.findByArticleId(articleId);
    }
}
