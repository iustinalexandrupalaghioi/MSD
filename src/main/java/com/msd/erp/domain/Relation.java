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
public class Relation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long _pk_relationid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phonenumber", nullable = false)
    private String phonenumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "relationtype", nullable = false)
    private RelationType relationtype;
}
