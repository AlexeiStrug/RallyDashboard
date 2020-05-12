package com.ge.dashboard.service.factory.handleData.impl;

import com.ge.dashboard.model.IterationEntity;
import com.ge.dashboard.service.factory.handleData.HandleDataByRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GetIterationsWithProjectName extends HandleDataByRequest<List<IterationEntity>> {

    public GetIterationsWithProjectName(JsonArray jsonElements, List<String> listOfIterationNames) {
        super(jsonElements, listOfIterationNames);
    }

    @Override
    public List<IterationEntity> getDataFromRequest() {
        List<IterationEntity> iterationEntities = convertJsonToIterationEntity(jsonElements);
        if(iterationEntities.size() > 10) {
            try {
                iterationEntities = iterationEntities.subList(iterationEntities.size() - 11, iterationEntities.size());
            } catch (IndexOutOfBoundsException e) {
                System.out.println("was exception");
            }
        }
        return iterationEntities;
    }
}
