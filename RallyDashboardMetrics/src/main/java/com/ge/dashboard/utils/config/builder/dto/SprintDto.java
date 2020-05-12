package com.ge.dashboard.utils.config.builder.dto;

import lombok.Data;

@Data
public class SprintDto {

    private Long id;
    private String iteration;
    private String type;
    private Double storyPoint;
}
