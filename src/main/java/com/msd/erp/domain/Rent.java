package com.msd.erp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonGetter;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rent")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentId;

    @NotNull(message = "Customer ID cannot be null")
    @ManyToOne
    @JoinColumn(name = "relationid")
    private Relation customer;

    @NotNull(message = "Start date cannot be null")
    private Date startDate;

    @NotNull(message = "End date cannot be null")
    private Date endDate;

    @NotNull(message = "Total price cannot be null")
    @Min(value = 0, message = "Total price cannot be negative")
    private Double totalPrice = 0.0;

    @NotNull(message = "Total price with penalties cannot be null")
    @Min(value = 0, message = "Total price with penalties cannot be negative")
    private Double totalPriceWithPenalties = 0.0;

    @NotNull(message = "Total price with VAT cannot be null")
    @Min(value = 0, message = "Total price with VAT cannot be negative")
    private Double totalPriceWithVAT = 0.0;

    @Min(value = 0, message = "Period must be a positive number")
    private Long period;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Rent state cannot be null")
    private RentState state = RentState.NEW;

    @JsonGetter("rentStateDescription")
    public String getTypeDescription() {
        return state.getDescription();
    }
}
