package com.msd.erp.application.computations;

import com.msd.erp.domain.PurchaseOrder;
import com.msd.erp.domain.PurchaseOrderLine;
import com.msd.erp.domain.SalesOrder;
import com.msd.erp.domain.SalesOrderLine;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrdersAmountsService {

    public BigDecimal calculatePurchaseLineAmount(PurchaseOrderLine line) {
        BigDecimal quantity = BigDecimal.valueOf(line.getQuantity());
        BigDecimal lineAmount = line.getArticle().getPrice().multiply(quantity);
        return lineAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal calculatePurchaseLineAmountWithVAT(PurchaseOrderLine line) {
        BigDecimal lineAmount = calculatePurchaseLineAmount(line);
        BigDecimal vatRate = line.getArticle().getVatid().getPercent();
        BigDecimal vatAmount = lineAmount.multiply(vatRate).divide(BigDecimal.valueOf(100), 2,
                BigDecimal.ROUND_HALF_UP);
        return lineAmount.add(vatAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal calculatePurchaseOrderAmmount(PurchaseOrder order) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (PurchaseOrderLine line : order.getPurchaseOrderLines()) {
            totalPrice.add(calculatePurchaseLineAmount(line));
        }
        return totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal calculatePurchaseOrderAmountWithVAT(PurchaseOrder order) {
        BigDecimal totalPriceWithVAT = BigDecimal.ZERO;
        for (PurchaseOrderLine line : order.getPurchaseOrderLines()) {
            totalPriceWithVAT = totalPriceWithVAT.add(calculatePurchaseLineAmountWithVAT(line));
        }
        return totalPriceWithVAT.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal calculateSalesLineAmount(SalesOrderLine line) {
        BigDecimal quantity = BigDecimal.valueOf(line.getQuantity());
        BigDecimal lineAmount = line.getArticle().getPrice().multiply(quantity);
        return lineAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal calculateSalesLineAmountWithVAT(SalesOrderLine line) {
        BigDecimal lineAmount = calculateSalesLineAmount(line);
        BigDecimal vatRate = line.getArticle().getVatid().getPercent();
        BigDecimal vatAmount = lineAmount.multiply(vatRate).divide(BigDecimal.valueOf(100), 2,
                BigDecimal.ROUND_HALF_UP);
        return vatAmount.add(lineAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal calculateSaleOrderAmount(SalesOrder order) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (SalesOrderLine line : order.getSalesOrderLines()) {
            totalPrice.add(calculateSalesLineAmount(line));
        }
        return totalPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal calculateSaleOrderAmountWithVAT(SalesOrder order) {
        BigDecimal totalPriceWithVAT = BigDecimal.ZERO;
        for (SalesOrderLine line : order.getSalesOrderLines()) {
            totalPriceWithVAT.add(calculateSalesLineAmountWithVAT(line));
        }
        return totalPriceWithVAT.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}
