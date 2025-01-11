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
import com.msd.erp.domain.PurchaseOrder;
import com.msd.erp.domain.PurchaseOrderLine;
import com.msd.erp.domain.PurchaseOrderState;
import com.msd.erp.infrastructure.repositories.ArticleRepository;
import com.msd.erp.infrastructure.repositories.PurchaseOrderLineRepository;
import com.msd.erp.infrastructure.repositories.PurchaseOrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseOrderLineService {

    private final PurchaseOrderLineRepository purchaseOrderLineRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ArticleRepository articleRepository;
    private final DomainValidationService validationService;
    private final OrdersAmountsService ordersAmountsService;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    public PurchaseOrderLine savePurchaseOrderLine(PurchaseOrderLine purchaseOrderLine) {
        Double lineAmount = ordersAmountsService.calculatePurchaseLineAmount(purchaseOrderLine);
        Double lineAmountWithVAT = ordersAmountsService.calculatePurchaseLineAmountWithVAT(purchaseOrderLine);

        purchaseOrderLine.setTotalLineAmount(lineAmount);
        purchaseOrderLine.setTotalLineAmountWithVAT(lineAmountWithVAT);

        return purchaseOrderLineRepository.save(purchaseOrderLine);
    }

    public PurchaseOrderLine createPurchaseOrderLine(PurchaseOrderLine purchaseOrderLine) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderLine.getPurchaseOrder().getPurchaseOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase order not found"));

        Article article = articleRepository.findById(purchaseOrderLine.getArticle().getArticleid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));

        purchaseOrderLine.setPurchaseOrder(purchaseOrder);
        purchaseOrderLine.setArticle(article);
        validationService.validateEntity(purchaseOrderLine);

        purchaseOrderService.updatePurchaseHeaderTotals(purchaseOrder, purchaseOrderLine.getTotalLineAmount(), purchaseOrderLine.getTotalLineAmountWithVAT());

        purchaseOrderService.savePurchaseOrder(purchaseOrder);
        return purchaseOrderLineRepository.save(purchaseOrderLine);
    }

    public List<PurchaseOrderLine> getAllPurchaseOrderLines() {
        return purchaseOrderLineRepository.findAll();
    }

    public Optional<PurchaseOrderLine> getPurchaseOrderLineById(Long id) {
        return purchaseOrderLineRepository.findById(id);
    }

    public PurchaseOrderLine updatePurchaseOrderLine(Long id, PurchaseOrderLine purchaseOrderLine) {
        PurchaseOrderLine existingPurchaseOrderLine = purchaseOrderLineRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase order line not found"));

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderLine.getPurchaseOrder().getPurchaseOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase order not found"));

        Article article = articleRepository.findById(purchaseOrderLine.getArticle().getArticleid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));

        Double oldLineAmount = existingPurchaseOrderLine.getTotalLineAmount();
        Double oldLineAmountWithVAT = existingPurchaseOrderLine.getTotalLineAmountWithVAT();

        purchaseOrderLine.setPurchaseOrder(purchaseOrder);
        purchaseOrderLine.setArticle(article);

        purchaseOrderService.updatePurchaseHeaderTotals(
                purchaseOrder,
                oldLineAmount,
                purchaseOrderLine.getTotalLineAmount(),
                oldLineAmountWithVAT,
                purchaseOrderLine.getTotalLineAmountWithVAT());

        purchaseOrderService.savePurchaseOrder(purchaseOrder);
        validationService.validateEntity(purchaseOrderLine);
        return purchaseOrderLineRepository.save(purchaseOrderLine);
    }

    @Transactional
    public void deletePurchaseOrderLine(Long id) {
        if (purchaseOrderLineRepository.existsById(id)) {
            purchaseOrderLineRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Purchase order line with ID " + id + " does not exist.");
        }
    }

    public boolean deletePurchaseOrderLineAndUpdatePurchaseOrder(Long purchaseOrderLineId) {
        Optional<PurchaseOrderLine> optionalPurchaseOrderLine = purchaseOrderLineRepository.findById(purchaseOrderLineId);
        if (optionalPurchaseOrderLine.isPresent()) {
            PurchaseOrderLine orderLine = optionalPurchaseOrderLine.get();
            PurchaseOrder purchaseOrder = orderLine.getPurchaseOrder();

            if (purchaseOrder.getState() == PurchaseOrderState.CONFIRMED ||
                    purchaseOrder.getState() == PurchaseOrderState.RECEIVED) {
                throw new IllegalStateException("Cannot delete purchase order lines from a purchase order in the " + purchaseOrder.getState() + " state.");
            }

            purchaseOrderService.updatePurchaseHeaderTotals(
                    purchaseOrder,
                    orderLine.getTotalLineAmount() * -1,
                    orderLine.getTotalLineAmountWithVAT() * -1);

            purchaseOrderService.savePurchaseOrder(purchaseOrder);

            purchaseOrderLineRepository.delete(orderLine);

            return true;
        }
        return false;
    }

    public boolean purchaseOrderLineExists(Long id) {
        return purchaseOrderLineRepository.existsById(id);
    }

    public List<PurchaseOrderLine> getPurchaseOrderLinesByPurchaseOrderId(Long purchaseOrderId) {
        return purchaseOrderLineRepository.findByPurchaseOrderId(purchaseOrderId);
    }

    public List<PurchaseOrderLine> getPurchaseOrderLinesByArticleId(Long articleId) {
        return purchaseOrderLineRepository.findByArticleId(articleId);
    }
}
