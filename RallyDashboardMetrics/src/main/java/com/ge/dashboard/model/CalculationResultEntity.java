package com.ge.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculationResultEntity {

    private String Vvalue;
    private Double calculationResult;
}
