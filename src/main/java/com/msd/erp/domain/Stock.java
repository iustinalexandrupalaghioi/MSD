package com.msd.erp.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    @OneToOne
    @JoinColumn(name = "articleid", unique = true)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Article article;

    @NotNull(message = "Available quantity cannot be null")
    @Min(value = 0, message = "Available quantity cannot be negative")
    private Integer availableQuantity = 0;

    @NotNull(message = "Incoming quantity cannot be null")
    @Min(value = 0, message = "Incoming quantity cannot be negative")
    private Integer incomingQuantity = 0;

    @NotNull(message = "Rented quantity cannot be null")
    @Min(value = 0, message = "Rented quantity cannot be negative")
    private Integer rentedQuantity = 0;

    @NotNull(message = "Technical quantity cannot be null")
    @Min(value = 0, message = "Technical quantity cannot be negative")
    private Integer technicalQuantity = 0;

}
