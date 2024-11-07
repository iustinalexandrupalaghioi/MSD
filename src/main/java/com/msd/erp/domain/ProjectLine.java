package com.msd.erp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "project_line")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectLineId;

    @NotNull(message = "Project cannot be null")
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @NotNull(message = "Article cannot be null")
    @ManyToOne
    @JoinColumn(name = "articleid", nullable = false)
    private Article article;

    @OneToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @OneToOne
    @JoinColumn(name = "purchase_order_line_id")
    private PurchaseOrderLine purchaseOrderLine;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    private Integer quantity = 0;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0")
    private Double price = 0.0;

    @NotNull(message = "Line amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Line amount must be greater than or equal to 0")
    private Double lineAmount = 0.0;
}
