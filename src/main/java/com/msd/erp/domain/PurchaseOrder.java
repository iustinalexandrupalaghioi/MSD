package com.msd.erp.domain;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchaseOrder")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseOrderId;

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
    @Digits(integer = 10, fraction = 2, message = "Total Price must be a valid decimal value with two decimal places")
    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @NotNull(message = "Total price with VAT cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total price with VAT must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Total price with VAT must be a valid decimal value with two decimal places")
    @Column(name = "total_price_with_vat", nullable = false)
    private BigDecimal totalPriceWithVAT;
}
