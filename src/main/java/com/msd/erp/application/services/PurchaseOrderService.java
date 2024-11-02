package com.msd.erp.application.services;

import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.PurchaseOrder;
import com.msd.erp.application.computations.OrdersAmountsService;
import com.msd.erp.infrastructure.repositories.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final DomainValidationService validationService;
    private final OrdersAmountsService ordersAmountsService;

    @Transactional
    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
        validationService.validateEntity(purchaseOrder);
        return purchaseOrderRepository.save(purchaseOrder);
    }

    public PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder) {
        BigDecimal totalAmount = ordersAmountsService.calculatePurchaseOrderAmmount(purchaseOrder);
        BigDecimal totalAmountWithVAT = ordersAmountsService.calculatePurchaseOrderAmountWithVAT(purchaseOrder);
        purchaseOrder.setTotalPrice(totalAmount);
        purchaseOrder.setTotalPriceWithVAT(totalAmountWithVAT);

        return purchaseOrderRepository.save(purchaseOrder);
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    public Optional<PurchaseOrder> getPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id);
    }

    @Transactional
    public Optional<PurchaseOrder> updatePurchaseOrder(Long id, PurchaseOrder updatedPurchaseOrder) {
        return purchaseOrderRepository.findById(id).map(existingPurchaseOrder -> {
            existingPurchaseOrder.setCustomerId(updatedPurchaseOrder.getCustomerId());
            existingPurchaseOrder.setProjectId(updatedPurchaseOrder.getProjectId());
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

    public boolean purchaseOrderExists(Long id) {
        return purchaseOrderRepository.existsById(id);
    }

    public List<PurchaseOrder> getPurchaseOrdersByCustomerId(Long customerId) {
        return purchaseOrderRepository.findByCustomerId(customerId);
    }

    public List<PurchaseOrder> getPurchaseOrdersByProjectId(Long projectId) {
        return purchaseOrderRepository.findByProjectId(projectId);
    }
}
