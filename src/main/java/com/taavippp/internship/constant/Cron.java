package com.taavippp.internship.constant;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

public final class Cron {
    /**
     * The actual Cron expression that Spring uses. 6 values separated by spaces.
    **/
    public static final String expression = "0 15 * ? * *";
    // public static final String expression = "0,30 * * ? * *";
    public static final long startupTime = Instant.now().getEpochSecond();

    /**
     * Returns the last execution time of the Cronjob or Optional.empty().
    **/
    public static Optional<Long> getLastExecutionUnixSeconds(long timestamp) {
        if (timestamp < startupTime || timestamp > Instant.now().getEpochSecond()) {
            return Optional.empty();
        }
        CronParser parser = new CronParser(
                CronDefinitionBuilder.instanceDefinitionFor(CronType.SPRING)
        );
        ExecutionTime executionTime = ExecutionTime.forCron(parser.parse(expression));
        ZonedDateTime time = ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(timestamp),
                ZoneId.of("UTC")
        );
        Optional<ZonedDateTime> lastExecution = executionTime.lastExecution(time);
        if (lastExecution.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(lastExecution.get().toEpochSecond());
    }
}
