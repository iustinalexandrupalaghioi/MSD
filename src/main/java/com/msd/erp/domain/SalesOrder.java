package com.msd.erp.domain;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.persistence.*;
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

    @NotNull(message = "Date cannot be null")
    @Column(name = "date", nullable = false)
    private Date date;

    @NotNull(message = "Total price cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total price must be greater than 0")
    @Column(name = "total_price", nullable = false)
    private Double totalPrice = 0.0;

    @NotNull(message = "Total price with VAT cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total price with VAT must be greater than 0")
    @Column(name = "total_price_with_vat", nullable = false)
    private Double totalPriceWithVAT = 0.0;

    // @OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<SalesOrderLine> salesOrderLines;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Sales order state cannot be null")
    private SalesOrderState state = SalesOrderState.NEW;

    @JsonGetter("salesOrderStateDescription")
    public String getTypeDescription() {
        return state.getDescription();
    }

}
