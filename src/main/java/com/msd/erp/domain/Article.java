package com.msd.erp.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "article")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long _pk_articleid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "um", nullable = false)
    private UMType um;

    @ManyToOne
    @JoinColumn(name = "_fk_categoryid", nullable = false)
    private Category categoryid;

    @ManyToOne
    @JoinColumn(name = "_fk_vatid", nullable = false)
    private VATRate vatid;
}
