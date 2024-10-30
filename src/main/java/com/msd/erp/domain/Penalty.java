package com.msd.erp.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "relation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Penalty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long _pk_penaltyid;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "penaltytype", nullable = false)
    private PenaltyType penaltytype;
}
