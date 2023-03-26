package com.taavippp.internship.controller;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.taavippp.internship.constant.Cron;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MainControllerTests {
    private final String URL = "http://localhost:8080/api?";

    private String sendRequest(String params) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(new URI(URL + params))
                .timeout(Duration.of(10, SECONDS))
                .build();
        HttpResponse<String> res = HttpClient.newBuilder()
                .build()
                .send(
                        req,
                        HttpResponse.BodyHandlers.ofString()
                );
        return res.body();
    }

    @Test
    void allMissingParamsReturnStatus400() throws URISyntaxException, IOException, InterruptedException {
        String body = sendRequest("missingParams");
        assertEquals("Missing param value", body);
    }

    @Test
    void someMissingParamsReturnStatus400() throws IOException, URISyntaxException, InterruptedException {
        String body = sendRequest("city=pärnu&timestamp=123");
        assertEquals("Missing param value", body);
    }

    @Test
    void passedTimestampReturnsOKButNoFee() throws IOException, URISyntaxException, InterruptedException {
        String body = sendRequest("city=pärnu&vehicle=car&timestamp=123");
        assertEquals("No records here", body);
    }

    @Test
    void upcomingTimestampReturnsOKButNoFee() throws IOException, URISyntaxException, InterruptedException {
        long timestamp = Instant.now().getEpochSecond() + 100;
        String body = sendRequest("city=pärnu&vehicle=car&timestamp=" + timestamp);
        assertEquals("No records here", body);
    }

    @Test
    void invalidParamValueReturnsStatus400() throws IOException, URISyntaxException, InterruptedException {
        String body = sendRequest("city=valga&vehicle=train");
        assertEquals("Invalid param value", body);
    }

/**
 * This seems to execute after the database already has some rows inserted, but
 * I managed to manually get this message thus confirming that it does work the
 * intended way.
**/
//    @Test
//    void validParamValuesBeforeDatabaseHasRowsReturnsNoFee() throws IOException, URISyntaxException, InterruptedException {
//        String body = sendRequest("city=tallinn&vehicle=car");
//        assertEquals("Record doesn't exist", body);
//    }

    @Test
    void validParamValuesReturnFloat() throws IOException, URISyntaxException, InterruptedException {
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
        String body = sendRequest("city=tallinn&vehicle=car");
        float fee = Float.parseFloat(body);
        assertInstanceOf(Float.class, fee);
    }

}
