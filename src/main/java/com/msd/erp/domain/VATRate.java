package com.msd.erp.domain;

import jakarta.persistence.*;
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
    private Long _pk_vatid;

    @Column(name = "percent", nullable = false)
    private double percent;
}
