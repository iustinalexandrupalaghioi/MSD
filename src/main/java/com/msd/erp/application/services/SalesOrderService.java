package com.msd.erp.application.services;

import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.SalesOrder;
import com.msd.erp.application.computations.OrdersAmountsService;
import com.msd.erp.infrastructure.repositories.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final DomainValidationService validationService;
    private final OrdersAmountsService ordersAmountsService;
    @Transactional
    public SalesOrder createSalesOrder(SalesOrder salesOrder) {
        validationService.validateEntity(salesOrder);
        BigDecimal totalAmount = ordersAmountsService.calculateSaleOrderAmount(salesOrder);
        BigDecimal totalAmountWithVAT = ordersAmountsService.calculateSaleOrderAmountWithVAT(salesOrder);
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

    @Transactional
    public Optional<SalesOrder> updateSalesOrder(Long id, SalesOrder updatedSalesOrder) {
        return salesOrderRepository.findById(id).map(existingSalesOrder -> {
            existingSalesOrder.setCustomerId(updatedSalesOrder.getCustomerId());
            existingSalesOrder.setProjectId(updatedSalesOrder.getProjectId());
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

    public boolean salesOrderExists(Long id) {
        return salesOrderRepository.existsById(id);
    }

    public List<SalesOrder> getSalesOrdersByCustomerId(Long customerId) {
        return salesOrderRepository.findByCustomerId(customerId);
    }

    public List<SalesOrder> getSalesOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return salesOrderRepository.findByDateRange(startDate, endDate);
    }
}
