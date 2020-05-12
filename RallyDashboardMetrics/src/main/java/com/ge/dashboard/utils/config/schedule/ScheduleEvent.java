package com.ge.dashboard.utils.config.schedule;

import com.ge.dashboard.service.impl.UserStoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleEvent {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleEvent.class);

    @Autowired
    private UserStoryServiceImpl userStoryService;

    @Scheduled(cron = "${cron.expression}")
    public void executeScheduleTaskCalculation() {
        LOGGER.debug("Execute task in method -> executeScheduleTaskCalculation()");
        userStoryService.calculateIterations();
        LOGGER.debug("Executed task in method -> executeScheduleTaskCalculation()");
    }

    @Scheduled(cron = "${cron.expression}")
    public void executeScheduleTaskRemainUSP() {
        LOGGER.debug("Execute task in method -> executeScheduleTaskRemainUSP()");
        userStoryService.remainStoryPoints();
        LOGGER.debug("Executed task in method -> executeScheduleTaskRemainUSP()");
    }

    @Scheduled(cron = "${cron.expression}")
    public void executeScheduleTaskCheckAndUpdateRelease() {
        LOGGER.debug("Execute task in method -> executeScheduleTaskCheckAndUpdateRelease()");
        userStoryService.checkAndUpdateReleaseState();
        LOGGER.debug("Executed task in method -> executeScheduleTaskCheckAndUpdateRelease()");
    }

}
