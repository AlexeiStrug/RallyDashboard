package com.ge.dashboard.utils.config.builder.dto;

import lombok.Data;

@Data
public class RemainDto {

    private Long id;
    private String iteration;
    private Double storyPointOfCalculation;
    private Double remainStoryPoints;
}
