package com.msd.erp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vatrate")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VATRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vat_id", nullable = false)
    private Long vatid;

    @NotNull(message = "Price cannot be null")
    @Column(name = "percent", nullable = false)
    private Double percent;
}
