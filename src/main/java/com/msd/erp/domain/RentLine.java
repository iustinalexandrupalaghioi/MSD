package com.msd.erp.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
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
@Table(name = "rent_line")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentLineId;

    @NotNull(message = "Rent cannot be null")
    @ManyToOne
    @JoinColumn(name = "rent_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Rent rent;

    @NotNull(message = "Article cannot be null")
    @ManyToOne
    @JoinColumn(name = "articleid", nullable = false)
    private Article article;


    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity = 0;

    @NotNull(message = "Price per day cannot be null")
    @Min(value = 0, message = "Price per day cannot be negative")
    private Double pricePerDay = 0.0;

    @NotNull(message = "Line amount cannot be null")
    @Min(value = 0, message = "Line amount cannot be negative")
    private Double lineAmount = 0.0;

    @NotNull(message = "Penalties amount with penalties cannot be null")
    @Min(value = 0, message = "Penalties amount with penalties cannot be negative")
    private Double penaltiesAmount = 0.0;

    @NotNull(message = "Line amount with penalties cannot be null")
    @Min(value = 0, message = "Line amount with penalties cannot be negative")
    private Double lineAmountWithPenalties = 0.0;

    @NotNull(message = "Line amount with VAT cannot be null")
    @Min(value = 0, message = "Line amount with VAT cannot be negative")
    private Double lineAmountWithVAT = 0.0;
}
