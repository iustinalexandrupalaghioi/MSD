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
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "applied_penalty")
@Data
public class AppliedPenalty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "RentLine cannot be null")
    @ManyToOne
    @JoinColumn(name = "rent_line_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RentLine rentLine;


    @NotNull(message = "Penalty cannot be null")
    @ManyToOne
    @JoinColumn(name = "penaltyid", nullable = false)
    private Penalty penalty;
}
