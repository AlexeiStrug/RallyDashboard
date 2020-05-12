package com.ge.dashboard.service.factory.handleData.impl;

import com.ge.dashboard.model.ReleaseEntity;
import com.ge.dashboard.service.factory.handleData.HandleDataByRequest;
import com.google.gson.JsonArray;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class GetReleases extends HandleDataByRequest<List<ReleaseEntity>> {


    public GetReleases(JsonArray jsonElements, List<String> listOfIterationNames) {
        super(jsonElements, listOfIterationNames);
    }

    @Override
    public List<ReleaseEntity> getDataFromRequest() {
        List<ReleaseEntity> releaseEntities = convertJsonToReleaseEntity(jsonElements);
        return releaseEntities.stream().filter(el -> (el.getReleaseDate() != null && el.getReleaseStartDate() != null)).collect(toList());
    }
}
