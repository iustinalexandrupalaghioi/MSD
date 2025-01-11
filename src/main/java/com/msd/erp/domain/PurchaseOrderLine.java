package com.msd.erp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "purchaseOrderLine")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseOrderLineId;

    @NotNull(message = "Purchase order cannot be null")
    @ManyToOne
    @JoinColumn(name = "_pk_purchaseOrderId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PurchaseOrder purchaseOrder;

    @NotNull(message = "Article cannot be null")
    @ManyToOne
    @JoinColumn(name = "_fk_articleId", nullable = false)
    private Article article;

    // @NotNull(message = "VAT ID cannot be null")
    // @Column(name = "_fk_vatId", nullable = false)
    // private String vatId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull(message = "Total line amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total line amount must be greater than or equal to 0")
    @Column(name = "total_line_amount", nullable = false)
    private Double totalLineAmount;

    @NotNull(message = "Total line amount with VAT cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total line amount with VAT must be greater than or equal to 0")
    @Column(name = "total_line_amount_with_vat", nullable = false)
    private Double totalLineAmountWithVAT;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0")
    @Column(name = "price", nullable = false)
    private Double price;
}
