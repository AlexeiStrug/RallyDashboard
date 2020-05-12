package com.ge.dashboard.service.factory.handleData;

import com.ge.dashboard.model.IterationEntity;
import com.ge.dashboard.model.ReleaseEntity;
import com.ge.dashboard.model.UserStoryEntity;
import com.ge.dashboard.utils.DateConversion;
import com.ge.dashboard.utils.config.SpringContext;
import com.google.gson.JsonArray;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

public abstract class HandleDataByRequest<T> {

    private DateConversion dateConversion = null;
    protected JsonArray jsonElements;
    protected List<String> listOfIterationNames;

    public HandleDataByRequest() {
    }

    public HandleDataByRequest(JsonArray jsonElements, List<String> listOfIterationNames) {
        ApplicationContext context = SpringContext.getAppContext();
        dateConversion = (DateConversion) context.getBean("dateConversion");

        this.jsonElements = jsonElements;
        this.listOfIterationNames = listOfIterationNames;
    }

    public abstract T getDataFromRequest();

    protected List<UserStoryEntity> convertJsonToUserStoryEntity(JsonArray jsonElements) {
        List<UserStoryEntity> userStories = new ArrayList<>();
        jsonElements.forEach(jsonElement -> {
            UserStoryEntity userStoryEntity = new UserStoryEntity();
            userStoryEntity.setFormattedId(!jsonElement.getAsJsonObject().get("FormattedID").toString().equals("null")  ? jsonElement.getAsJsonObject().get("FormattedID").getAsString() : "");
            userStoryEntity.setScheduleState(!jsonElement.getAsJsonObject().get("ScheduleState").toString().equals("null") ? jsonElement.getAsJsonObject().get("ScheduleState").getAsString() : "");
            userStoryEntity.setIterationName(!jsonElement.getAsJsonObject().get("Iteration").toString().equals("null") ? jsonElement.getAsJsonObject().get("Iteration").getAsJsonObject().get("_refObjectName").getAsString() : "");
            userStoryEntity.setName(!jsonElement.getAsJsonObject().get("Name").toString().equals("null") ? jsonElement.getAsJsonObject().get("Name").getAsString() : "");
            userStoryEntity.setPlanEstimate(!jsonElement.getAsJsonObject().get("PlanEstimate").toString().equals("null") ? jsonElement.getAsJsonObject().get("PlanEstimate").getAsDouble() : 0.0);
            userStories.add(userStoryEntity);
        });
        return userStories;
    }

    protected List<IterationEntity> convertJsonToIterationEntity(JsonArray jsonElements) {
        List<IterationEntity> iterationEntities = new ArrayList<>();
        jsonElements.forEach(jsonElement -> {
            IterationEntity iterationEntity = new IterationEntity();
            iterationEntity.setName(!jsonElement.getAsJsonObject().get("Name").toString().equals("null")  ? jsonElement.getAsJsonObject().get("Name").getAsString() : "");
            iterationEntity.setStartDate(!jsonElement.getAsJsonObject().get("StartDate").toString().equals("null")  ? dateConversion.getStringCurrentDate(jsonElement.getAsJsonObject().get("StartDate").getAsString().substring(0, 10)) : null);
            iterationEntity.setEndDate(!jsonElement.getAsJsonObject().get("EndDate").toString().equals("null")  ? dateConversion.getStringCurrentDate(jsonElement.getAsJsonObject().get("EndDate").getAsString().substring(0, 10)) : null);
            iterationEntities.add(iterationEntity);
        });
        return iterationEntities;
    }

    protected List<ReleaseEntity> convertJsonToReleaseEntity(JsonArray jsonElements) {
        List<ReleaseEntity> releaseEntities = new ArrayList<>();
        jsonElements.forEach(jsonElement -> {
            ReleaseEntity releaseEntity = new ReleaseEntity();
            releaseEntity.setReleaseName(!jsonElement.getAsJsonObject().get("Name").toString().equals("null")  ? jsonElement.getAsJsonObject().get("Name").getAsString() : "");
            releaseEntity.setReleaseStartDate(!jsonElement.getAsJsonObject().get("ReleaseStartDate").toString().equals("null")  ? dateConversion.getStringCurrentDate(jsonElement.getAsJsonObject().get("ReleaseStartDate").getAsString().substring(0, 10)) : null);
            releaseEntity.setReleaseDate(!jsonElement.getAsJsonObject().get("ReleaseDate").toString().equals("null") ? dateConversion.getStringCurrentDate(jsonElement.getAsJsonObject().get("ReleaseDate").getAsString().substring(0, 10)) : null);
            releaseEntity.setState(!jsonElement.getAsJsonObject().get("State").toString().equals("null") ? jsonElement.getAsJsonObject().get("State").getAsString() : "");
            releaseEntities.add(releaseEntity);
        });
        return releaseEntities;
    }

    public String currentProjectName() {
        return !jsonElements.get(0).getAsJsonObject().get("Name").toString().equals("null") ? jsonElements.get(0).getAsJsonObject().get("Name").getAsString() : "";
    }
}
