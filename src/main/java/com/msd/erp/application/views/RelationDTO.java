package com.msd.erp.application.views;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class RelationDTO {
    private Long id;
    private String name;
    private String relationType;
    private String country;
    private String address;
    private String email;
    private String phonenumber;
}
