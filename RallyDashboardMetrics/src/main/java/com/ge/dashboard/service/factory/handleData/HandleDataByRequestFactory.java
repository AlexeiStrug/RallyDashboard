package com.ge.dashboard.service.factory.handleData;

import com.ge.dashboard.model.enums.TypeOfRequestEnum;
import com.ge.dashboard.service.factory.handleData.impl.*;
import com.ge.dashboard.utils.exception.TypeOfRequestProcessingException;
import com.google.gson.JsonArray;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HandleDataByRequestFactory {

    public static HandleDataByRequest handleData(TypeOfRequestEnum typeOfRequest, JsonArray jsonElements, List<String> listOfIterationNames) throws TypeOfRequestProcessingException {
        Optional<TypeOfRequestEnum> typeOfRequestEnumOptional = Arrays.stream(TypeOfRequestEnum.values())
                .filter(value -> value.name().equals(typeOfRequest.name()))
                .findFirst();
        HandleDataByRequest result = null;
        switch (Objects.requireNonNull(typeOfRequestEnumOptional.orElse(null))) {
            case ITERATION:
                result = new GetIterationsWithProjectName(jsonElements, null);
                break;
            case SP_BACKLOG:
                result = new GetStoryPointFromBacklog(jsonElements, null);
                break;
            case SP_PER_ITERATION_MAP:
                result = new GetStoryPointPerIterationMap(jsonElements, listOfIterationNames);
                break;
            case SP_PER_ITERATION_LIST:
                result = new GetStoryPointPerIterationList(jsonElements, listOfIterationNames);
                break;
            case RELEASE:
                result = new GetReleases(jsonElements, null);
                break;
            case PROJECT:
                result = new GetProjectName(jsonElements, null);
                break;
            default:
                throw new TypeOfRequestProcessingException("Sorry! Next type of request -> " + typeOfRequest.getName() + " does not supported yet.");
        }
        return result;
    }
}
