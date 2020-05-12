package com.ge.dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStoryEntity {

    private String formattedId;
    private String scheduleState;
    private String iterationName;
    private String name;
    private Double planEstimate;

}
