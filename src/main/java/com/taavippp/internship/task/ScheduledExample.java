package com.taavippp.internship.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledExample {

    private static final Logger log = LoggerFactory.getLogger(ScheduledExample.class);

    @Scheduled(fixedRate = 2000)
    public void example() {
        log.info("Task @" + System.currentTimeMillis() / 1000);
    }

}
