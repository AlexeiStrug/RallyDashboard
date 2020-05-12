package com.ge.dashboard.service.factory.handleData.impl;

import com.ge.dashboard.model.UserStoryEntity;
import com.ge.dashboard.service.factory.handleData.HandleDataByRequest;
import com.google.gson.JsonArray;

import java.util.List;

import static java.util.stream.Collectors.summarizingDouble;

public class GetStoryPointFromBacklog extends HandleDataByRequest<Double> {

    public GetStoryPointFromBacklog(JsonArray jsonElements, List<String> listOfIterationNames) {
        super(jsonElements, listOfIterationNames);
    }

    @Override
    public Double getDataFromRequest() {
        List<UserStoryEntity> userStories = convertJsonToUserStoryEntity(jsonElements);
        return userStories.stream().collect(summarizingDouble(UserStoryEntity::getPlanEstimate)).getSum();
    }
}
