package com.ge.dashboard.service;

import com.ge.dashboard.utils.config.builder.CustomResponse;
import com.ge.dashboard.utils.exception.TypeOfResponseTypeProcessingException;

public interface UserStoryService {

    CustomResponse getSprintData(String projectId) throws TypeOfResponseTypeProcessingException;

    CustomResponse getReleaseData(String projectId) throws TypeOfResponseTypeProcessingException;

    CustomResponse getRemainData(String projectId) throws TypeOfResponseTypeProcessingException;

    void calculateIterations();

    void remainStoryPoints();

    void checkAndUpdateReleaseState();
}
