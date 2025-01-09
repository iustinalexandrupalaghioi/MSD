package com.msd.erp.application.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.msd.erp.application.computations.OrdersAmountsService;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.SalesOrder;
import com.msd.erp.domain.SalesOrderLine;
import com.msd.erp.domain.SalesOrderState;
import com.msd.erp.domain.Stock;
import com.msd.erp.infrastructure.repositories.SalesOrderLineRepository;
import com.msd.erp.infrastructure.repositories.SalesOrderRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final DomainValidationService validationService;
    private final OrdersAmountsService ordersAmountsService;
    private final StockService stockService;
    private final SalesOrderLineRepository salesOrderLineRepository;


    public SalesOrder createSalesOrder(SalesOrder salesOrder) {
        validationService.validateEntity(salesOrder);
        Double totalAmount = ordersAmountsService.calculateSaleOrderAmount(salesOrder);
        Double totalAmountWithVAT = ordersAmountsService.calculateSaleOrderAmountWithVAT(salesOrder);
        salesOrder.setTotalPrice(totalAmount);
        salesOrder.setTotalPriceWithVAT(totalAmountWithVAT);
        return salesOrderRepository.save(salesOrder);
    }

    public SalesOrder saveSalesOrder(SalesOrder salesOrder) {
        // Calculează și actualizează totalurile înainte de a salva comanda
        return salesOrderRepository.save(salesOrder);
    }

    public List<SalesOrder> getAllSalesOrders() {
        return salesOrderRepository.findAll();
    }

    public Optional<SalesOrder> getSalesOrderById(Long id) {
        return salesOrderRepository.findById(id);
    }

    public void updateSalesHeaderTotals(
            SalesOrder salesOrder, Double newLineAmount, Double newLineAmountWithVAT) {
        updateSalesHeaderTotals(salesOrder, 0.0, newLineAmount, 0.0, newLineAmountWithVAT);
    }

    public void updateSalesHeaderTotals(
            SalesOrder salesOrder, Double oldLineAmount, Double newLineAmount,
            Double oldLineAmountWithVAT, Double newLineAmountWithVAT
           ) {

        salesOrder.setTotalPrice(salesOrder.getTotalPrice() - oldLineAmount + newLineAmount);
        salesOrder.setTotalPriceWithVAT(salesOrder.getTotalPriceWithVAT() - oldLineAmountWithVAT + newLineAmountWithVAT);
    }

    @Transactional
    public Optional<SalesOrder> updateSalesOrder(Long id, SalesOrder updatedSalesOrder) {
        return salesOrderRepository.findById(id).map(existingSalesOrder -> {
            existingSalesOrder.setCustomerId(updatedSalesOrder.getCustomerId());
            existingSalesOrder.setDate(updatedSalesOrder.getDate());
            existingSalesOrder.setTotalPrice(updatedSalesOrder.getTotalPrice());
            existingSalesOrder.setTotalPriceWithVAT(updatedSalesOrder.getTotalPriceWithVAT());

            validationService.validateEntity(existingSalesOrder);
            return salesOrderRepository.save(existingSalesOrder);
        });
    }

    @Transactional
    public void deleteSalesOrder(Long id) {
        if (salesOrderRepository.existsById(id)) {
            salesOrderRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Sales order with ID " + id + " does not exist.");
        }
    }

    public SalesOrder save(SalesOrder salesOrder) {
        validationService.validateEntity(salesOrder);
        return salesOrderRepository.save(salesOrder);
    }

    public boolean salesOrderExists(Long id) {
        return salesOrderRepository.existsById(id);
    }

    public List<SalesOrder> getSalesOrdersByCustomerId(Long customerId) {
        return salesOrderRepository.findByCustomerId(customerId);
    }

    public List<SalesOrder> getSalesOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return salesOrderRepository.findByDateRange(startDate, endDate);
    }

    public boolean validateStockForSalesOrder(Long salesOrderId) {
        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Sales order with ID " + salesOrderId + " not found."));

        if (!salesOrder.getState().equals(SalesOrderState.NEW)) {
            throw new IllegalStateException("Sales order is not in a valid state for stock validation.");
        }

        List<SalesOrderLine> salesOrderLines = salesOrderLineRepository.findBySalesOrderId(salesOrderId);

        for (SalesOrderLine salesOrderLine : salesOrderLines) {
            Stock stock = stockService.findByArticle(salesOrderLine.getArticle())
                    .orElseThrow(() -> new EntityNotFoundException("Stock for article " + salesOrderLine.getArticle().getArticleid() + " - " + salesOrderLine.getArticle().getName() + " not found."));

            if (stock.getAvailableQuantity() < salesOrderLine.getQuantity()) {
                return false;
            }
        }

        return true;
    }

    // Confirm sales order
    public void confirmSalesOrder(Long salesOrderId) {
        Optional<SalesOrder> optionalSalesOrder = salesOrderRepository.findById(salesOrderId);

        if (optionalSalesOrder.isPresent()) {
            SalesOrder existingSalesOrder = optionalSalesOrder.get();

            if (existingSalesOrder.getState() != SalesOrderState.NEW) {
                throw new IllegalStateException("Sales order can only be confirmed from the NEW state.");
            }

            List<SalesOrderLine> salesOrderLines = salesOrderLineRepository.findBySalesOrderId(salesOrderId);

            if (salesOrderLines.isEmpty()) {
            throw new IllegalStateException("Sales order cannot be confirmed because it has no order lines.");
        }

            existingSalesOrder.setState(SalesOrderState.CONFIRMED);
            salesOrderRepository.save(existingSalesOrder);
        } else {
            throw new EntityNotFoundException("Sales order not found.");
        }
    }

    // Mark sales order as sent
    public void markAsSent(Long salesOrderId) {
        Optional<SalesOrder> optionalSalesOrder = salesOrderRepository.findById(salesOrderId);

        if (optionalSalesOrder.isPresent()) {
            SalesOrder existingSalesOrder = optionalSalesOrder.get();

            if (existingSalesOrder.getState() != SalesOrderState.CONFIRMED) {
                throw new IllegalStateException("Sales order can only be marked as sent after being confirmed.");
            }

            existingSalesOrder.setState(SalesOrderState.SENT);
            salesOrderRepository.save(existingSalesOrder);
        } else {
            throw new EntityNotFoundException("Sales order not found.");
        }
    }

    // Mark sales order as delivered
    public void markAsDelivered(Long salesOrderId) {
        Optional<SalesOrder> optionalSalesOrder = salesOrderRepository.findById(salesOrderId);

        if (optionalSalesOrder.isPresent()) {
            SalesOrder existingSalesOrder = optionalSalesOrder.get();

            if (existingSalesOrder.getState() != SalesOrderState.SENT) {
                throw new IllegalStateException("Sales order can only be marked as delivered after being sent.");
            }

            existingSalesOrder.setState(SalesOrderState.DELIVERED);
            salesOrderRepository.save(existingSalesOrder);
        } else {
            throw new EntityNotFoundException("Sales order not found.");
        }
    }

    // Cancel sales order
    public void cancelSalesOrder(Long salesOrderId) {
        Optional<SalesOrder> optionalSalesOrder = salesOrderRepository.findById(salesOrderId);

        if (optionalSalesOrder.isPresent()) {
            SalesOrder existingSalesOrder = optionalSalesOrder.get();

            if (existingSalesOrder.getState() == SalesOrderState.SENT || 
                existingSalesOrder.getState() == SalesOrderState.DELIVERED) {
                throw new IllegalStateException("Sales order cannot be cancelled after being sent or delivered.");
            }

            existingSalesOrder.setState(SalesOrderState.CANCELLED);
            salesOrderRepository.save(existingSalesOrder);
        } else {
            throw new EntityNotFoundException("Sales order not found.");
        }
    }
}
