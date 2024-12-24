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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    private Long relationid;

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 100, message = "Name must be less than or equal to 100 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Country cannot be empty")
    @Size(max = 100, message = "Country must be less than or equal to 100 characters")
    @Column(name = "country", nullable = false)
    private String country;

    @NotBlank(message = "Address cannot be empty")
    @Size(max = 255, message = "Address must be less than or equal to 255 characters")
    @Column(name = "address", nullable = false)
    private String address;

    @NotBlank(message = "E-mail cannot be empty")
    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank(message = "Phone number cannot be empty")
    @Size(max = 15, message = "Phone number must be less than or equal to 15 characters")
    @Column(name = "phonenumber", nullable = false)
    private String phonenumber;

    @NotNull(message = "Relation type cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "relationtype", nullable = false)
    private RelationType relationtype;

    @JsonGetter("relationTypeDescription")
    public String getRelationTypeDescription() {
        return relationtype != null ? relationtype.getDescription() : null;   
    }
}
