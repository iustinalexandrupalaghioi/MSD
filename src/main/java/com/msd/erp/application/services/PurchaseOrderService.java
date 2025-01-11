package com.msd.erp.application.services;

import com.msd.erp.application.computations.OrdersAmountsService;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.*;
import com.msd.erp.infrastructure.repositories.PurchaseOrderLineRepository;
import com.msd.erp.infrastructure.repositories.PurchaseOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final DomainValidationService validationService;
    private final OrdersAmountsService ordersAmountsService;
    private final StockService stockService;
    private final PurchaseOrderLineRepository purchaseOrderLineRepository;

    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
        validationService.validateEntity(purchaseOrder);
        Double totalAmount = ordersAmountsService.calculatePurchaseOrderAmount(purchaseOrder);
        Double totalAmountWithVAT = ordersAmountsService.calculatePurchaseOrderAmountWithVAT(purchaseOrder);
        purchaseOrder.setTotalPrice(totalAmount);
        purchaseOrder.setTotalPriceWithVAT(totalAmountWithVAT);
        return purchaseOrderRepository.save(purchaseOrder);
    }

    public PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderRepository.save(purchaseOrder);
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    public Optional<PurchaseOrder> getPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id);
    }

    public void updatePurchaseHeaderTotals(
            PurchaseOrder purchaseOrder, Double newLineAmount, Double newLineAmountWithVAT) {
        updatePurchaseHeaderTotals(purchaseOrder, 0.0, newLineAmount, 0.0, newLineAmountWithVAT);
    }

    public void updatePurchaseHeaderTotals(
            PurchaseOrder purchaseOrder, Double oldLineAmount, Double newLineAmount,
            Double oldLineAmountWithVAT, Double newLineAmountWithVAT
    ) {

        purchaseOrder.setTotalPrice(purchaseOrder.getTotalPrice() - oldLineAmount + newLineAmount);
        purchaseOrder.setTotalPriceWithVAT(purchaseOrder.getTotalPriceWithVAT() - oldLineAmountWithVAT + newLineAmountWithVAT);
    }

    @Transactional
    public Optional<PurchaseOrder> updatePurchaseOrder(Long id, PurchaseOrder updatedPurchaseOrder) {
        return purchaseOrderRepository.findById(id).map(existingPurchaseOrder -> {
            existingPurchaseOrder.setSupplierId(updatedPurchaseOrder.getSupplierId());
            existingPurchaseOrder.setDate(updatedPurchaseOrder.getDate());
            existingPurchaseOrder.setTotalPrice(updatedPurchaseOrder.getTotalPrice());
            existingPurchaseOrder.setTotalPriceWithVAT(updatedPurchaseOrder.getTotalPriceWithVAT());

            validationService.validateEntity(existingPurchaseOrder);
            return purchaseOrderRepository.save(existingPurchaseOrder);
        });
    }

    @Transactional
    public void deletePurchaseOrder(Long id) {
        if (purchaseOrderRepository.existsById(id)) {
            purchaseOrderRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Purchase order with ID " + id + " does not exist.");
        }
    }
    public PurchaseOrder save(PurchaseOrder purchaseOrder) {
        validationService.validateEntity(purchaseOrder);
        return purchaseOrderRepository.save(purchaseOrder);
    }

    public boolean purchaseOrderExists(Long id) {
        return purchaseOrderRepository.existsById(id);
    }

    public List<PurchaseOrder> getPurchaseOrdersBySupplierId(Long supplierId) {
        return purchaseOrderRepository.findBySupplierId(supplierId);
    }

    public List<PurchaseOrder> getPurchaseOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return purchaseOrderRepository.findByDateRange(startDate, endDate);
    }

    public boolean validateStockForPurchaseOrder(Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Purchase order with ID " + purchaseOrderId + " not found."));

        if (!purchaseOrder.getState().equals(SalesOrderState.NEW)) {
            throw new IllegalStateException("Purchase order is not in a valid state for stock validation.");
        }

        List<PurchaseOrderLine> purchaseOrderLines = purchaseOrderLineRepository.findByPurchaseOrderId(purchaseOrderId);

        for (PurchaseOrderLine purchaseOrderLine : purchaseOrderLines) {
            Stock stock = stockService.findByArticle(purchaseOrderLine.getArticle())
                    .orElseThrow(() -> new EntityNotFoundException("Stock for article " + purchaseOrderLine.getArticle().getArticleid() + " - " + purchaseOrderLine.getArticle().getName() + " not found."));

            if (stock.getAvailableQuantity() < purchaseOrderLine.getQuantity()) {
                return false;
            }
        }

        return true;
    }

    // Confirm purchase order
    public void confirmPurchaseOrder(Long purchaseOrderId) {
        Optional<PurchaseOrder> optionalPurchaseOrder = purchaseOrderRepository.findById(purchaseOrderId);

        if (optionalPurchaseOrder.isPresent()) {
            PurchaseOrder existingPurchaseOrder = optionalPurchaseOrder.get();

            if (existingPurchaseOrder.getState() != PurchaseOrderState.NEW) {
                throw new IllegalStateException("Purchase order can only be confirmed from the NEW state.");
            }

            List<PurchaseOrderLine> purchaseOrderLines = purchaseOrderLineRepository.findByPurchaseOrderId(purchaseOrderId);

            if (purchaseOrderLines.isEmpty()) {
                throw new IllegalStateException("Purchase order cannot be confirmed because it has no order lines.");
            }

            existingPurchaseOrder.setState(PurchaseOrderState.CONFIRMED);
            purchaseOrderRepository.save(existingPurchaseOrder);
        } else {
            throw new EntityNotFoundException("Purchase order not found.");
        }
    }

    // Mark purchase order as received
    public void markAsReceived(Long purchaseOrderId) {
        Optional<PurchaseOrder> optionalPurchaseOrder = purchaseOrderRepository.findById(purchaseOrderId);

        if (optionalPurchaseOrder.isPresent()) {
            PurchaseOrder existingPurchaseOrder = optionalPurchaseOrder.get();

            if (existingPurchaseOrder.getState() != PurchaseOrderState.CONFIRMED) {
                throw new IllegalStateException("Purchase order can only be marked as received after being confirmed.");
            }

            existingPurchaseOrder.setState(PurchaseOrderState.RECEIVED);
            purchaseOrderRepository.save(existingPurchaseOrder);
        } else {
            throw new EntityNotFoundException("Purchase order not found.");
        }
    }

    // Mark sales order as sent to supplier
    public void markAsSentToSupplier(Long purchaseOrderId) {
        Optional<PurchaseOrder> optionalPurchaseOrder = purchaseOrderRepository.findById(purchaseOrderId);

        if (optionalPurchaseOrder.isPresent()) {
            PurchaseOrder existingPurchaseOrder = optionalPurchaseOrder.get();

            if (existingPurchaseOrder.getState() != PurchaseOrderState.SENT) {
                throw new IllegalStateException("Purchase order can only be marked as sent after being confirmed.");
            }

            existingPurchaseOrder.setState(PurchaseOrderState.RECEIVED);
            purchaseOrderRepository.save(existingPurchaseOrder);
        } else {
            throw new EntityNotFoundException("Purchase order not found.");
        }
    }

    // Cancel purchase order
    public void cancelPurchaseOrder(Long purchaseOrderId) {
        Optional<PurchaseOrder> optionalPurchaseOrder = purchaseOrderRepository.findById(purchaseOrderId);

        if (optionalPurchaseOrder.isPresent()) {
            PurchaseOrder existingPurchaseOrder = optionalPurchaseOrder.get();

            if (existingPurchaseOrder.getState() == PurchaseOrderState.RECEIVED) {
                throw new IllegalStateException("Purchase order cannot be cancelled after being received.");
            }

            existingPurchaseOrder.setState(PurchaseOrderState.CANCELLED);
            purchaseOrderRepository.save(existingPurchaseOrder);
        } else {
            throw new EntityNotFoundException("Purchase order not found.");
        }
    }
}
