package com.msd.erp.domain;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "salesOrderLine")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salesOrderLineId;

    @NotNull(message = "Sales order cannot be null")
    @ManyToOne
    @JoinColumn(name = "_pk_salesOrderId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SalesOrder salesOrder;

    @NotNull(message = "Article cannot be null")
    @ManyToOne
    @JoinColumn(name = "_fk_articleId", nullable = false)
    private Article article;

//    @NotNull(message = "VAT ID cannot be null")
//    @Column(name = "_fk_vatId", nullable = false)
//    private String vatId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    @NotNull(message = "Total line amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total line amount must be greater than or equal to 0")
    @Column(name = "total_line_amount", nullable = false)
    private Double totalLineAmount = 0.0;

    @NotNull(message = "Total line amount with VAT cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total line amountt with VAT must be greater than or equal to 0")
    @Column(name = "total_line_amount_with_vat", nullable = false)
    private Double totalLineAmountWithVAT = 0.0;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0")
    @Column(name = "price", nullable = false)
    private Double price = 0.0;
}
