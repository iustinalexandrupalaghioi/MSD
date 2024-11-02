package com.msd.erp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "article")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleid;

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 100, message = "Name must be less than or equal to 100 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Description cannot be empty")
    @Size(max = 255, message = "Description must be less than or equal to 255 characters")
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price must be a valid decimal value with two decimal places")
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @NotNull(message = "Unit of Measure (UM) cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "um", nullable = false)
    private UMType um;

    @NotNull(message = "CategoryID cannot be null")
    @ManyToOne
    @JoinColumn(name = "_fk_categoryid", nullable = false)
    private Category categoryid;

    @NotNull(message = "VATRateID cannot be null")
    @ManyToOne
    @JoinColumn(name = "_fk_vatid", nullable = false)
    private VATRate vatid;
}
