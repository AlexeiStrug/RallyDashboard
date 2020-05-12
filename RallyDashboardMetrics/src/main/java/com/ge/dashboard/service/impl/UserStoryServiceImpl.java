package com.ge.dashboard.service.impl;

import com.ge.dashboard.utils.config.builder.CustomResponse;
import com.ge.dashboard.utils.config.builder.enums.ResponseDataType;
import com.ge.dashboard.utils.config.builder.enums.ResponseType;
import com.ge.dashboard.model.*;
import com.ge.dashboard.model.enums.TypeOfCalculation;
import com.ge.dashboard.model.enums.TypeOfQueryEnum;
import com.ge.dashboard.model.enums.TypeOfRequestEnum;
import com.ge.dashboard.repository.DashboardRepository;
import com.ge.dashboard.repository.ProjectRepository;
import com.ge.dashboard.repository.ReleaseRepository;
import com.ge.dashboard.service.UserStoryService;
import com.ge.dashboard.service.factory.calculationData.CalculateData;
import com.ge.dashboard.service.factory.calculationData.CalculateDataFactory;
import com.ge.dashboard.service.factory.handleData.HandleDataByRequest;
import com.ge.dashboard.service.factory.handleData.HandleDataByRequestFactory;
import com.ge.dashboard.utils.config.builder.DtoResponseBuilder;
import com.ge.dashboard.utils.DateConversion;
import com.ge.dashboard.utils.config.rally.RallyApiConfig;
import com.ge.dashboard.utils.exception.TypeOfRequestProcessingException;
import com.ge.dashboard.utils.exception.TypeOfResponseTypeProcessingException;
import com.google.gson.JsonArray;
import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.response.QueryResponse;
import com.rallydev.rest.util.QueryFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserStoryServiceImpl implements UserStoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserStoryServiceImpl.class);

    @Value("#{'${rally.list.of.projects}'.split(',')}")
    private List<String> projectList;
    private String currentProcessingProjectName = "";

    private final RallyApiConfig rallyApiService;
    private final DashboardRepository dashboardRepository;
    private final ReleaseRepository releaseRepository;
    private final ProjectRepository projectRepository;
    private final DtoResponseBuilder responseBuilder;
    private final DateConversion dateConversion;

    @Autowired
    public UserStoryServiceImpl(DtoResponseBuilder responseBuilder, ProjectRepository projectRepository, RallyApiConfig rallyApiService, DashboardRepository dashboardRepository, ReleaseRepository releaseRepository, DateConversion dateConversion) {
        this.responseBuilder = responseBuilder;
        this.projectRepository = projectRepository;
        this.rallyApiService = rallyApiService;
        this.dashboardRepository = dashboardRepository;
        this.releaseRepository = releaseRepository;
        this.dateConversion = dateConversion;
    }

    @Override
    public CustomResponse getSprintData(String projectId) throws TypeOfResponseTypeProcessingException {
        LOGGER.debug("Find project by projectId -> getSprintData()");
        Optional<ProjectEntity> projectEntity = projectRepository.getByProjectId(projectId);
        if (projectEntity.isEmpty()) throw new NoSuchElementException();
        LOGGER.debug("Get all iterations by projectId from database in method -> getDashboardData()");
        List<DashboardEntity> dashboardEntities = dashboardRepository.getAllByProject(projectEntity.get());
        LOGGER.debug("Found next data -> DashboardEntity -> " + dashboardEntities);
        return dashboardEntities.size() > 0 ? responseBuilder.buildResponse(ResponseType.SUCCESS, responseBuilder.buildResponseDto(dashboardEntities, ResponseDataType.SPRINT)) : responseBuilder.buildResponse(ResponseType.WARNING, null);
    }

    @Override
    public CustomResponse getReleaseData(String projectId) throws TypeOfResponseTypeProcessingException {
        LOGGER.debug("Find project by projectId -> getReleaseData()");
        Optional<ProjectEntity> projectEntity = projectRepository.getByProjectId(projectId);
        if (projectEntity.isEmpty()) throw new NoSuchElementException();
        LOGGER.debug("Get all releases by projectId from database in method -> getReleaseData()");
        List<ReleaseEntity> releaseEntities = releaseRepository.getAllByProject(projectEntity.get());
        LOGGER.debug("Found next data -> ReleaseEntity -> " + releaseEntities);
        return releaseEntities.size() > 0 ? responseBuilder.buildResponse(ResponseType.SUCCESS, responseBuilder.buildResponseDto(releaseEntities, ResponseDataType.RELEASE)) : responseBuilder.buildResponse(ResponseType.WARNING, null);
    }


    @Override
    public CustomResponse getRemainData(String projectId) throws TypeOfResponseTypeProcessingException {
        LOGGER.debug("Find project by projectId -> getRemainData()");
        Optional<ProjectEntity> projectEntity = projectRepository.getByProjectId(projectId);
        if (projectEntity.isEmpty()) throw new NoSuchElementException();
        LOGGER.debug("Get all releases by projectId from database in method -> getRemainData()");
        List<DashboardEntity> releaseEntities = dashboardRepository.getAllByProject(projectEntity.get());
        LOGGER.debug("Found next data -> ReleaseEntity -> " + releaseEntities);
        return releaseEntities.size() > 0 ? responseBuilder.buildResponse(ResponseType.SUCCESS, responseBuilder.buildResponseDto(releaseEntities, ResponseDataType.REMAIN)) : responseBuilder.buildResponse(ResponseType.WARNING, null);
    }

    @Override
    public void calculateIterations() {
        LOGGER.debug("In the method -> calculateIterations()");
        CustomResponse response = new CustomResponse();
        LOGGER.debug("Execute calculation for iteration in method -> calculateIterations()");
        RallyRestApi rallyRestApi = rallyApiService.getRallyConnection();
        LOGGER.debug("Rally connection object rallyRestApi -> " + rallyRestApi);
        projectList.parallelStream().forEach(project -> {
            try {
                LOGGER.debug("Current project is -> " + project);
                List<IterationEntity> listOfIterations = getListOfIterations(rallyRestApi, project);
                LOGGER.debug("List of Iterations -> " + listOfIterations);
                if (project != null) {
                    saveNewProject(rallyRestApi, project);
                }
                IterationEntity currentIteration = listOfIterations.get(listOfIterations.size() - 1);
                listOfIterations.remove(currentIteration);
                LOGGER.debug("Current iteration -> " + currentIteration);
                List<String> listOfIterationNames = listOfIterationNames(listOfIterations);
//        Map<String, Double> mapOfStoryPointPerIteration = getMapOfStoryPointPerIteration(rallyRestApi, listOfIterationNames, project);
                List<Double> listOfStoryPointPerIteration = getListOfStoryPointPerIteration(rallyRestApi, listOfIterationNames, project);
                LOGGER.debug("Current list of story points per iteration is -> " + listOfStoryPointPerIteration);
                Double storyPointFromBacklog = getStoryPointFromBacklog(rallyRestApi, project);
//                Double storyPointFromBacklog = 10.0;
                if (listOfStoryPointPerIteration.size() == 0 || storyPointFromBacklog == 0.0)
                    throw new NoSuchElementException();
                LOGGER.debug("Story point from backlog -> " + storyPointFromBacklog);
                CalculationResultEntity resultOfCalculation = generateResultOfVvalue(listOfStoryPointPerIteration, storyPointFromBacklog);
                LOGGER.debug("Result of calculation for project -> " + currentIteration + " result data -> " + resultOfCalculation);
                saveDashboardData(currentIteration, storyPointFromBacklog, project, resultOfCalculation);
            } catch (TypeOfRequestProcessingException e) {
                LOGGER.error("TypeOfRequestProcessingException error message -> " + e.getMessage() + " , stacktrace -> " + Arrays.toString(e.getStackTrace()));
                response.setMessage("TypeOfRequestProcessingException error message -> " + e.getMessage());
                response.setResponseType(ResponseType.ERROR);
            } catch (Exception e) {
                LOGGER.error("Something gone wrong error message -> " + e.getMessage() + " , stacktrace -> " + Arrays.toString(e.getStackTrace()));
                response.setMessage("Something gone wrong error message -> " + e.getMessage() + " , stacktrace -> " + Arrays.toString(e.getStackTrace()));
                response.setResponseType(ResponseType.ERROR);
            }
        });
    }

    @Override
    public void remainStoryPoints() {
        LOGGER.debug("In the method -> remainStoryPoints()");
        RallyRestApi rallyRestApi = rallyApiService.getRallyConnection();
        LOGGER.debug("Rally connection object rallyRestApi -> " + rallyRestApi);
        projectList.parallelStream().forEach(project -> {
            Date currentDate = dateConversion.getStringCurrentDate();
            DashboardEntity dashboardEntity = dashboardRepository.fetchDashboardByProjectIdAndEndDate(project, currentDate);
            if (dashboardEntity == null) throw new NoSuchElementException();
            List<Double> listOfStoryPointForOneIteration = null;
            try {
                listOfStoryPointForOneIteration = getListOfStoryPointPerIteration(rallyRestApi, Collections.singletonList(dashboardEntity.getIteration()), project);
            } catch (TypeOfRequestProcessingException e) {
                e.printStackTrace();
            }
            if (listOfStoryPointForOneIteration == null) throw new NoSuchElementException();
            saveDashboardData(dashboardEntity, Objects.requireNonNull(listOfStoryPointForOneIteration.get(0)));
        });
    }

    @Override
    public void checkAndUpdateReleaseState() {
        LOGGER.debug("In the method -> checkAndUpdateReleaseState()");
        RallyRestApi rallyRestApi = rallyApiService.getRallyConnection();
        LOGGER.debug("Rally connection object rallyRestApi -> " + rallyRestApi);
        projectList.parallelStream().forEach(project -> {
            if (project != null) {
                try {
                    saveNewProject(rallyRestApi, project);
                } catch (TypeOfRequestProcessingException e) {
                    e.printStackTrace();
                }
            }
            Date currentDate = dateConversion.getStringCurrentDate();
            List<ReleaseEntity> releaseEntities = null;
            try {
                releaseEntities = getReleases(rallyRestApi, project);
            } catch (TypeOfRequestProcessingException e) {
                e.printStackTrace();
            }
            LOGGER.debug("checkAndUpdateReleaseState() - releaseEntities -> " + releaseEntities);
            saveOrUpdateReleases(currentDate, releaseEntities, project);
        });
    }

    private void saveOrUpdateReleases(Date currentDate, List<ReleaseEntity> releaseEntities, String project) {
        if (releaseEntities == null) throw new NoSuchElementException();
        releaseEntities
                .parallelStream()
                .forEach(el -> saveNewReleases(el, project));
        List<ReleaseEntity> delayReleases = releaseEntities
                .stream()
                .filter(el -> el.getReleaseDate() != null && currentDate.after(el.getReleaseDate()) && !el.getState().equals("Accepted"))
                .collect(Collectors.toList());
        delayReleases
                .parallelStream()
                .forEach(delayRelease -> updateDiffDateOrStateForDelayRelease(delayRelease, currentDate));
        releaseEntities.removeAll(delayReleases);
        releaseEntities
                .parallelStream()
                .forEach(releaseEntity -> updateDiffDateOrStateForDelayRelease(releaseEntity, currentDate));
    }

    private void saveNewReleases(ReleaseEntity releaseEntity, String project) {
        LOGGER.debug("Try to save data for release -> " + releaseEntity);
        ReleaseEntity findReleaseEntity = releaseRepository.getByReleaseName(releaseEntity.getReleaseName());
        if (findReleaseEntity == null) {
            LOGGER.debug("Saving data -> saveOrUpdateRelease() for newReleaseEntity");
            ReleaseEntity newReleaseEntity = new ReleaseEntity();
            newReleaseEntity.setReleaseStartDate(releaseEntity.getReleaseStartDate());
            newReleaseEntity.setReleaseDate(releaseEntity.getReleaseDate());
            newReleaseEntity.setReleaseName(releaseEntity.getReleaseName());
            newReleaseEntity.setState(releaseEntity.getState());
            newReleaseEntity.setDayOfDiff(releaseEntity.getDayOfDiff());
            newReleaseEntity.setProject(Optional.of(projectRepository.getByProjectId(project)).get().orElseThrow());
            releaseRepository.saveAndFlush(newReleaseEntity);
            LOGGER.debug("Data successfully saved -> saveOrUpdateRelease() -> " + newReleaseEntity);
        }
    }

    private synchronized void saveNewProject(RallyRestApi rallyRestApi, String projectId) throws TypeOfRequestProcessingException {
        Optional<ProjectEntity> findProject = projectRepository.getByProjectId(projectId);
        if (findProject.isEmpty()) {
            String projectName = getProjectNameByProjectId(rallyRestApi, projectId);
            if (projectName != null) {
                ProjectEntity projectEntity = new ProjectEntity();
                projectEntity.setProjectId(projectId);
                projectEntity.setProjectName(projectName);
                projectRepository.saveAndFlush(projectEntity);
            }
        }
    }

    private void updateDiffDateOrStateForDelayRelease(ReleaseEntity releaseEntity, Date currentDate) {
        LOGGER.debug("Try to update data for release -> " + releaseEntity + " current date -> " + currentDate);
        ReleaseEntity findReleaseEntity = releaseRepository.getByReleaseName(releaseEntity.getReleaseName());
        LOGGER.debug("updateDiffDateOrStateForDelayRelease() - findReleaseEntity -> " + findReleaseEntity);
        if (findReleaseEntity != null && findReleaseEntity.getState().equals(releaseEntity.getState()) && !findReleaseEntity.getState().equals("Accepted")) {
            LOGGER.debug("Updating  data -> updateDiffDateOrStateForDelayRelease() for findReleaseEntity non accepted releases and out of date");
            findReleaseEntity.setDayOfDiff(dateConversion.getDateDiff(findReleaseEntity.getReleaseDate(), currentDate, TimeUnit.DAYS));
            releaseRepository.saveAndFlush(findReleaseEntity);
            LOGGER.debug("Data successfully saved -> updateDiffDateOrStateForDelayRelease() non accepted releases and out of date -> " + findReleaseEntity);
        } else if (findReleaseEntity != null && !findReleaseEntity.getState().equals(releaseEntity.getState())) {
            LOGGER.debug("Updating data -> saveOrUpdateRelease() for findReleaseEntity where state was changed to Accepted");
            findReleaseEntity.setState(releaseEntity.getState());
            findReleaseEntity.setDayOfDiff(dateConversion.getDateDiff(findReleaseEntity.getReleaseDate(), currentDate, TimeUnit.DAYS));
            releaseRepository.saveAndFlush(findReleaseEntity);
            LOGGER.debug("Data successfully updated -> updateDiffDateOrStateForDelayRelease() for findReleaseEntity where state was changed to Accepted -> " + findReleaseEntity);
        }
    }

    private void saveDashboardData(IterationEntity currentIteration, Double storyPointFromBacklog, String project, CalculationResultEntity resultOfCalculation) {
        LOGGER.debug("Try to save data for project -> " + currentIteration + " result data -> " + resultOfCalculation);
        DashboardEntity existIteration = dashboardRepository.getByIteration(currentIteration.getName());
        if (existIteration == null) {
            LOGGER.debug("Saving data -> saveDashboardData()");
            DashboardEntity dashboardEntity = new DashboardEntity();
            dashboardEntity.setIteration(currentIteration.getName());
            dashboardEntity.setProject(Optional.of(projectRepository.getByProjectId(project)).get().orElseThrow());
            dashboardEntity.setType(resultOfCalculation.getVvalue());
            dashboardEntity.setStoryPoint(storyPointFromBacklog);
            dashboardEntity.setStoryPointOfCalculation(resultOfCalculation.getCalculationResult());
            dashboardEntity.setStartDate(currentIteration.getStartDate());
            dashboardEntity.setEndDate(currentIteration.getEndDate());
            dashboardRepository.saveAndFlush(dashboardEntity);
            LOGGER.debug("Data successfully saved -> saveDashboardData() -> " + dashboardEntity);
        } else {
            LOGGER.error("Found next project -> saveDashboardData() -> " + existIteration);
        }
    }

    private void saveDashboardData(DashboardEntity dashboardEntity, Double storyPointFromIteration) {
        dashboardEntity.setRemainStoryPoints(dashboardEntity.getStoryPoint() - storyPointFromIteration);
        dashboardRepository.saveAndFlush(dashboardEntity);
    }


    private CalculationResultEntity generateResultOfVvalue(List<Double> listOfStoryPointPerIteration, Double storyPointFromBacklog) throws TypeOfRequestProcessingException {
        CalculationResultEntity resultEntity = new CalculationResultEntity();
        CalculateData calculateData = CalculateDataFactory.calculateData(TypeOfCalculation.MIN, listOfStoryPointPerIteration);
        double vmin = calculateData.calculate();
        if (vmin >= storyPointFromBacklog) {
            calculateData = CalculateDataFactory.calculateData(TypeOfCalculation.AVG, listOfStoryPointPerIteration);
            double vavg = calculateData.calculate();
            if (vmin > storyPointFromBacklog && vavg > storyPointFromBacklog) {
                resultEntity.setCalculationResult(vmin);
                resultEntity.setVvalue(TypeOfCalculation.MIN.getName());
            } else {
                calculateData = CalculateDataFactory.calculateData(TypeOfCalculation.MAX, listOfStoryPointPerIteration);
                double vmax = calculateData.calculate();
                if (vavg >= storyPointFromBacklog && vmax > storyPointFromBacklog) {
                    resultEntity.setCalculationResult(vavg);
                    resultEntity.setVvalue(TypeOfCalculation.AVG.getName());
                } else {
                    resultEntity.setCalculationResult(vmax);
                    resultEntity.setVvalue(TypeOfCalculation.MAX.getName());
                }
            }
        } else {
            resultEntity.setCalculationResult(vmin);
            resultEntity.setVvalue(TypeOfCalculation.BELOW.getName());
        }
        return resultEntity;
    }

    private List<IterationEntity> getListOfIterations(RallyRestApi rallyRestApi, String currentProcessingProject) throws TypeOfRequestProcessingException {
        JsonArray queryResponseIterations = getQueryResponse(rallyRestApi, TypeOfQueryEnum.ITERATION, currentProcessingProject, null);
        HandleDataByRequest handleDataByRequest = HandleDataByRequestFactory.handleData(TypeOfRequestEnum.ITERATION, queryResponseIterations, null);
//        currentProcessingProjectName = handleDataByRequest.currentProjectName();
        return (List<IterationEntity>) Objects.requireNonNull(handleDataByRequest).getDataFromRequest();
    }

    //FIXME temporary disabled
    private Map<String, Double> getMapOfStoryPointPerIteration(RallyRestApi rallyRestApi, List<String> listOfIterations, String currentProcessingProject) throws TypeOfRequestProcessingException {
        QueryFilter queryFilter = new QueryFilter("Iteration.Name", "=", listOfIterations.get(0));
        for (String el : listOfIterations) {
            queryFilter = queryFilter.or(new QueryFilter("Iteration.Name", "=", el));
        }
        JsonArray queryResponseIterations = getQueryResponse(rallyRestApi, TypeOfQueryEnum.HIERARCHICAL, currentProcessingProject, queryFilter);
        HandleDataByRequest handleDataByRequest = HandleDataByRequestFactory.handleData(TypeOfRequestEnum.SP_PER_ITERATION_MAP, queryResponseIterations, listOfIterations);
        return (Map<String, Double>) Objects.requireNonNull(handleDataByRequest).getDataFromRequest();
    }

    private List<Double> getListOfStoryPointPerIteration(RallyRestApi rallyRestApi, List<String> listOfIterations, String currentProcessingProject) throws TypeOfRequestProcessingException {
        QueryFilter queryFilter = new QueryFilter("Iteration.Name", "=", listOfIterations.get(0));
        for (String el : listOfIterations) {
            queryFilter = queryFilter.or(new QueryFilter("Iteration.Name", "=", el));
        }
        JsonArray queryResponseIterations = getQueryResponse(rallyRestApi, TypeOfQueryEnum.HIERARCHICAL, currentProcessingProject, queryFilter);
        HandleDataByRequest handleDataByRequest = HandleDataByRequestFactory.handleData(TypeOfRequestEnum.SP_PER_ITERATION_LIST, queryResponseIterations, listOfIterations);
        return (List<Double>) Objects.requireNonNull(handleDataByRequest).getDataFromRequest();
    }

    private Double getStoryPointFromBacklog(RallyRestApi rallyRestApi, String currentProcessingProject) throws TypeOfRequestProcessingException {
        QueryFilter queryFilter = new QueryFilter("ScheduleState", "=", "Defined")
                .and(new QueryFilter("Blocked", "=", "false"))
                .and(new QueryFilter("Iteration", "=", "null"))
                .and(new QueryFilter("c_PreDevelopmentStatus", "=", "4. Ready to Develop"));
        JsonArray queryResponseIterations = getQueryResponse(rallyRestApi, TypeOfQueryEnum.HIERARCHICAL, currentProcessingProject, queryFilter);
        HandleDataByRequest handleDataByRequest = HandleDataByRequestFactory.handleData(TypeOfRequestEnum.SP_BACKLOG, queryResponseIterations, null);
        return (Double) Objects.requireNonNull(handleDataByRequest).getDataFromRequest();
    }

    private List<ReleaseEntity> getReleases(RallyRestApi rallyRestApi, String currentProcessingProject) throws TypeOfRequestProcessingException {
        JsonArray queryResponseIterations = getQueryResponse(rallyRestApi, TypeOfQueryEnum.RELEASE, currentProcessingProject, null);
        HandleDataByRequest handleDataByRequest = HandleDataByRequestFactory.handleData(TypeOfRequestEnum.RELEASE, queryResponseIterations, null);
        return (List<ReleaseEntity>) Objects.requireNonNull(handleDataByRequest).getDataFromRequest();
    }

    private String getProjectNameByProjectId(RallyRestApi rallyRestApi, String currentProcessingProject) throws TypeOfRequestProcessingException {
        QueryFilter queryFilter = new QueryFilter("ObjectID", "=", currentProcessingProject.substring(9));
        JsonArray queryResponseProjectName = getQueryResponse(rallyRestApi, TypeOfQueryEnum.PROJECT, currentProcessingProject, queryFilter);
        HandleDataByRequest handleDataByRequest = HandleDataByRequestFactory.handleData(TypeOfRequestEnum.PROJECT, queryResponseProjectName, null);
        return (String) Objects.requireNonNull(handleDataByRequest).getDataFromRequest();
    }

    private synchronized JsonArray getQueryResponse(RallyRestApi rallyRestApi, TypeOfQueryEnum typeOfQueryEnum, String currentProcessingProject, QueryFilter queryFilter) {
        QueryResponse queryResponse = null;
        QueryRequest queryRequest = null;
        try {
            queryRequest = rallyApiService.setQueryRequest(currentProcessingProject, typeOfQueryEnum.getName(), queryFilter);
        } catch (TypeOfRequestProcessingException e) {
            e.printStackTrace();
        }
        try {
            if (queryRequest != null) {
                queryResponse = rallyRestApi.query(queryRequest);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(queryResponse).getResults().getAsJsonArray();
    }

    private List<String> listOfIterationNames(List<IterationEntity> iterationEntities) {
        if (iterationEntities.size() != 0) {
            return iterationEntities.stream().map(IterationEntity::getName).collect(Collectors.toList());
        } else {
            throw new NoSuchElementException();
        }
    }
}
