package com.ge.dashboard.utils.config.builder.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReleaseDto {

    private Long id;
    private String releaseName;
    private Date releaseStartDate;
    private Date releaseDate;
    private String state;
    private long dayOfDiff;
}
