package com.taavippp.internship.task;


import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.taavippp.internship.constant.Cron;
import com.taavippp.internship.repository.WeatherConditionsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class WeatherDataOperationTests {

    @Autowired
    WeatherConditionsRepository weatherConditionsRepository;

    @Test
    void WaitUntilFirstExecutionThenCheckIf3DatabaseRowsWereAdded() throws InterruptedException {
        CronParser parser = new CronParser(
                CronDefinitionBuilder.instanceDefinitionFor(CronType.SPRING)
        );
        ExecutionTime executionTime = ExecutionTime.forCron(
                parser.parse(Cron.expression)
        );
        Optional<Duration> timeToNextOpt = executionTime.timeToNextExecution(
                ZonedDateTime.now()
        );
        assertNotEquals(Optional.empty(), timeToNextOpt.get());
        // Timeout is 10 seconds
        Duration timeToNext = timeToNextOpt.get().plusSeconds(10);
        Thread.sleep(timeToNext.toMillis());
        long count = weatherConditionsRepository.count();
        assertEquals(3, count);
    }

}
