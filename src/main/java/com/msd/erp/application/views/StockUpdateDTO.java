package com.msd.erp.application.views;

import lombok.Data;

@Data
public class StockUpdateDTO {
    private Integer incomingQuantity;
    private Integer rentedQuantity;
    private Integer receivedQuantity;
    private Integer returnedQuantity;
    private Integer soldQuantity;
    private Integer availableQuantity;
     private Integer canceledQuantity;
}
