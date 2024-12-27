package com.msd.erp.domain;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "salesOrder")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class SalesOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salesOrderId;

    @NotNull(message = "CustomerId cannot be null")
    @ManyToOne
    @JoinColumn(name = "_fk_customerId", nullable = false)
    private Relation customerId;

    @NotNull(message = "ProjectId cannot be null")
    @ManyToOne
    @JoinColumn(name = "_fk_projectId", nullable = false)
    private Project ProjectId;

    @NotNull(message = "Date cannot be null")
    @Column(name = "date", nullable = false)
    private Date date;

    @NotNull(message = "Total price cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total price must be greater than 0")
    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @NotNull(message = "Total price with VAT cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total price with VAT must be greater than 0")
    @Column(name = "total_price_with_vat", nullable = false)
    private Double totalPriceWithVAT;

    // @OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<SalesOrderLine> salesOrderLines;

}
