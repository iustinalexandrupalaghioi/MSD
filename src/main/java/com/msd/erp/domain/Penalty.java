package com.msd.erp.domain;

import com.fasterxml.jackson.annotation.JsonGetter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @JsonGetter("penaltyTypeDescription")
    public String getTypeDescription() {
        return penaltytype.getDescription();
    }
}
