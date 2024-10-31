package com.msd.erp.application.views;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PenaltyDTO {
    private Long id;
    private String description;
    private String penaltyType;
}
