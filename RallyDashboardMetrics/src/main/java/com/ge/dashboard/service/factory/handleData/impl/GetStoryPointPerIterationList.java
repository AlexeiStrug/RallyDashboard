package com.ge.dashboard.service.factory.handleData.impl;

import com.ge.dashboard.model.UserStoryEntity;
import com.ge.dashboard.service.factory.handleData.HandleDataByRequest;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.summarizingDouble;

public class GetStoryPointPerIterationList extends HandleDataByRequest<List<Double>> {


    public GetStoryPointPerIterationList(JsonArray jsonElements, List<String> listOfIterationNames) {
        super(jsonElements, listOfIterationNames);
    }

    @Override
    public List<Double> getDataFromRequest() {
        List<Double> listOfStoryPointPerIteration = new ArrayList<>();
        List<UserStoryEntity> userStories = convertJsonToUserStoryEntity(jsonElements);

        for (String iteration : listOfIterationNames) {
            Double sumSpForOneIteration = userStories.stream().filter(el -> el.getIterationName().equals(iteration) && el.getScheduleState().equals("Accepted") && !el.getName().contains("[Continued]")).collect(summarizingDouble(UserStoryEntity::getPlanEstimate)).getSum();
            listOfStoryPointPerIteration.add(sumSpForOneIteration);
        }

        return listOfStoryPointPerIteration;
    }
}
