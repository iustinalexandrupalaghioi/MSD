package com.msd.erp.domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private LocalDateTime date;

    @NotNull(message = "Total price cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total price must be greater than 0")
    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @NotNull(message = "Total price with VAT cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total price with VAT must be greater than 0")
    @Column(name = "total_price_with_vat", nullable = false)
    private Double totalPriceWithVAT;

    @OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SalesOrderLine> salesOrderLines;

}
