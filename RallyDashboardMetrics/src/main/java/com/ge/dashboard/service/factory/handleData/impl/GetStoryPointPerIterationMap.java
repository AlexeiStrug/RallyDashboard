package com.ge.dashboard.service.factory.handleData.impl;

import com.ge.dashboard.model.UserStoryEntity;
import com.ge.dashboard.service.factory.handleData.HandleDataByRequest;
import com.google.gson.JsonArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.summarizingDouble;

public class GetStoryPointPerIterationMap extends HandleDataByRequest<Map<String, Double>> {

    public GetStoryPointPerIterationMap(JsonArray jsonElements, List<String> listOfIterationNames) {
        super(jsonElements, listOfIterationNames);
    }

    @Override
    public Map<String, Double> getDataFromRequest() {
        Map<String, Double> mapOfStoryPointPerIteration = new HashMap<>();

        List<UserStoryEntity> userStories = convertJsonToUserStoryEntity(jsonElements);

        for (String iteration : listOfIterationNames) {
            Double sumSpForOneIteration = userStories.stream().filter(el -> el.getIterationName().equals(iteration) && el.getScheduleState().equals("Accepted") && !el.getName().contains("[Continued]")).collect(summarizingDouble(UserStoryEntity::getPlanEstimate)).getSum();
            mapOfStoryPointPerIteration.put(iteration, sumSpForOneIteration);
        }

        return mapOfStoryPointPerIteration;
    }
}
