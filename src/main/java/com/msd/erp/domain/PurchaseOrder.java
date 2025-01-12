package com.msd.erp.domain;
import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Entity
@Table(name = "purchaseOrder")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseOrderId;

    @NotNull(message = "SupplierId cannot be null")
    @ManyToOne
    @JoinColumn(name = "_fk_supplierId", nullable = false)
    private Relation supplierId;

    // @NotNull(message = "ProjectId cannot be null")
    // @ManyToOne
    // @JoinColumn(name = "_fk_projectId", nullable = false)
    // private Project projectId;

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

    // @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<PurchaseOrderLine> purchaseOrderLines;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Purchase order state cannot be null")
    private PurchaseOrderState state = PurchaseOrderState.NEW;

    @JsonGetter("purchaseOrderStateDescription")
    public String getTypeDescription() {
        return state.getDescription();
    }
}
