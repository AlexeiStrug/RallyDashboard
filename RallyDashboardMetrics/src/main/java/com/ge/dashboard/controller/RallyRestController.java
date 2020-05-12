package com.ge.dashboard.controller;

import com.ge.dashboard.utils.config.builder.CustomResponse;
import com.ge.dashboard.utils.config.builder.enums.ResponseType;
import com.ge.dashboard.service.impl.UserStoryServiceImpl;
import com.ge.dashboard.utils.exception.TypeOfResponseTypeProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.NoSuchElementException;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RallyRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RallyRestController.class);

    @Autowired
    private UserStoryServiceImpl userStoryService;


    @GetMapping("/sprint-data")
    public ResponseEntity getSprintData(@RequestParam(value = "projectId", required = true) String projectId) {
        try {
            CustomResponse response = userStoryService.getSprintData(projectId);
            LOGGER.debug("Get response from controller for getSprintData method: " + response);
            return ResponseEntity.status(response.getResponseType().equals(ResponseType.SUCCESS) ? HttpStatus.OK : HttpStatus.CONFLICT)
                    .body(response);
        } catch (NoSuchElementException | TypeOfResponseTypeProcessingException e) {
            LOGGER.error("Error during getSprintData: message -> " + e.getMessage() + ", stacktrace -> " + Arrays.toString(e.getStackTrace()));
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error during getSprintData: message -> " + e.getMessage() + ", stacktrace -> " + Arrays.toString(e.getStackTrace()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @GetMapping("/release-data")
    public ResponseEntity getReleaseData(@RequestParam(value = "projectId", required = true) String projectId) {
        try {
            CustomResponse response = userStoryService.getReleaseData(projectId);
            LOGGER.debug("Get response from controller for getReleaseData method: " + response);
            return ResponseEntity.status(response.getResponseType().equals(ResponseType.SUCCESS) ? HttpStatus.OK : HttpStatus.CONFLICT)
                    .body(response);
        } catch (NoSuchElementException | TypeOfResponseTypeProcessingException e) {
            LOGGER.error("Error during getReleaseData: message -> " + e.getMessage() + ", stacktrace -> " + Arrays.toString(e.getStackTrace()));
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error during getReleaseData: message -> " + e.getMessage() + ", stacktrace -> " + Arrays.toString(e.getStackTrace()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @GetMapping("/remain-data")
    public ResponseEntity getRemainData(@RequestParam(value = "projectId", required = true) String projectId) {
        try {
            CustomResponse response = userStoryService.getRemainData(projectId);
            LOGGER.debug("Get response from controller for getRemainData method: " + response);
            return ResponseEntity.status(response.getResponseType().equals(ResponseType.SUCCESS) ? HttpStatus.OK : HttpStatus.CONFLICT)
                    .body(response);
        } catch (NoSuchElementException e) {
            LOGGER.error("Error during getRemainData: message -> " + e.getMessage() + ", stacktrace -> " + Arrays.toString(e.getStackTrace()));
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .build();
        } catch (Exception e) {
            LOGGER.error("Error during getRemainData: message -> " + e.getMessage() + ", stacktrace -> " + Arrays.toString(e.getStackTrace()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @GetMapping(path = "/iteration")
    public void calculationIteration() {
        userStoryService.calculateIterations();
    }

    @GetMapping(path = "/remain")
    public void remainCalculation() {
        userStoryService.remainStoryPoints();
    }

    @GetMapping(path = "/releases")
    public void getReleases() {
        userStoryService.checkAndUpdateReleaseState();
    }
}
