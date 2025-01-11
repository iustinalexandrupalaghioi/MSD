package com.msd.erp.application.computations;

import org.springframework.stereotype.Service;

import com.msd.erp.domain.PurchaseOrder;
import com.msd.erp.domain.PurchaseOrderLine;
import com.msd.erp.domain.SalesOrder;
import com.msd.erp.domain.SalesOrderLine;


@Service
public class OrdersAmountsService {

    public Double calculatePurchaseLineAmount(PurchaseOrderLine line) {
        Integer quantity = line.getQuantity();
        Double lineAmount = line.getArticle().getPrice() * quantity;
        return lineAmount;
    }

    public Double calculatePurchaseLineAmountWithVAT(PurchaseOrderLine line) {
        Double lineAmount = calculatePurchaseLineAmount(line);
        Double vatRate = line.getArticle().getVatid().getPercent();
        Double vatAmount = lineAmount * vatRate / 100;
        return lineAmount + vatAmount;
    }

    public Double calculatePurchaseOrderAmount(PurchaseOrder order) {
        Double totalPrice = 0.0;
        // for (SalesOrderLine line : order.getSalesOrderLines()) {
        //     totalPrice = totalPrice + (calculateSalesLineAmount(line));
        // }
        return totalPrice;
    }

    public Double calculatePurchaseOrderAmountWithVAT(PurchaseOrder order) {
        Double totalPriceWithVAT = 0.0;
        // for (SalesOrderLine line : order.getSalesOrderLines()) {
        //     totalPriceWithVAT = totalPriceWithVAT + (calculateSalesLineAmountWithVAT(line));
        // }
        return totalPriceWithVAT;
    }

    public Double calculateSalesLineAmount(SalesOrderLine line) {
        Integer quantity = line.getQuantity();
        Double lineAmount = line.getArticle().getPrice() * quantity;
        return lineAmount;
    }

    public Double calculateSalesLineAmountWithVAT(SalesOrderLine line) {
        Double lineAmount = calculateSalesLineAmount(line);
        Double vatRate = line.getArticle().getVatid().getPercent();
        Double vatAmount = lineAmount * vatRate / 100;
        return vatAmount + lineAmount;
    }

    public Double calculateSaleOrderAmount(SalesOrder order) {
        Double totalPrice = 0.0;
        // for (SalesOrderLine line : order.getSalesOrderLines()) {
        //     totalPrice = totalPrice + (calculateSalesLineAmount(line));
        // }
        return totalPrice;
    }

    public Double calculateSaleOrderAmountWithVAT(SalesOrder order) {
        Double totalPriceWithVAT = 0.0;
        // for (SalesOrderLine line : order.getSalesOrderLines()) {
        //     totalPriceWithVAT = totalPriceWithVAT + (calculateSalesLineAmountWithVAT(line));
        // }
        return totalPriceWithVAT;
    }

}
