package com.ge.dashboard.service.factory.handleData.impl;

import com.ge.dashboard.service.factory.handleData.HandleDataByRequest;
import com.google.gson.JsonArray;

import java.util.List;

public class GetProjectName extends HandleDataByRequest<String> {


    public GetProjectName(JsonArray jsonElements, List<String> listOfIterationNames) {
        super(jsonElements, listOfIterationNames);
    }

    @Override
    public String getDataFromRequest() {
        return currentProjectName();
    }
}
