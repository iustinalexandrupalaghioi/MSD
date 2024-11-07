package com.msd.erp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "penalty")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Penalty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long penaltyid;

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 100, message = "Description must be less than or equal to 100 characters")
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull(message = "Penalty type cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "penaltytype", nullable = false)
    private PenaltyType penaltytype;

    @NotNull(message = "Price cannot be null")
    @Column(name = "price", nullable = false)
    private Double price = 0.0;
}
