package com.ge.dashboard.utils.config.rally;

import com.ge.dashboard.utils.exception.TypeOfRequestProcessingException;
import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.util.Fetch;
import com.rallydev.rest.util.QueryFilter;


public abstract class RallyApiConfig {

    public abstract RallyRestApi getRallyConnection();

    public QueryRequest setQueryRequest(String projectID, String request, QueryFilter queryFilter) throws TypeOfRequestProcessingException {
        QueryRequest queryRequest = new QueryRequest(request);
        Fetch fetch = null;
        switch (request) {
            case "Iteration":
                fetch = new Fetch("Name", "StartDate", "EndDate", "Project");
                break;
            case "HierarchicalRequirement":
                fetch = new Fetch("PlanEstimate", "FormattedID", "Iteration", "ScheduleState", "Name");
                break;
            case "Release":
                fetch = new Fetch("Name", "ReleaseStartDate", "ReleaseDate", "State");
                break;
            case "Project":
                fetch = new Fetch("Name");
                break;
            default:
                throw new TypeOfRequestProcessingException("Sorry! Next type of request -> " + request + " does not supported yet.");
        }
        queryRequest.setFetch(fetch);
        queryRequest.setScopedDown(false);
        queryRequest.setScopedUp(false);
        queryRequest.setProject(projectID);
        if (queryFilter != null) queryRequest.setQueryFilter(queryFilter);
        return queryRequest;
    }
}
