package com.taavippp.internship.controller;

import com.taavippp.internship.constant.Cron;
import com.taavippp.internship.constant.FeeData;
import com.taavippp.internship.model.City;
import com.taavippp.internship.model.Vehicle;
import com.taavippp.internship.model.WeatherConditions;
import com.taavippp.internship.repository.WeatherConditionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping(MainController.path)
public class MainController {
    public static final String path = "/api";

    private static final ResponseEntity<String> forbiddenVehicleResponse =
            new ResponseEntity<>("Usage of selected vehicle type is " +
                    "forbidden", HttpStatus.OK);

    @Autowired
    WeatherConditionsRepository weatherConditionsRepository;

    @GetMapping()
    public ResponseEntity<String> getDeliveryFee(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String vehicle,
            @RequestParam(required = false) Long timestamp
    ) {
        if (city == null || vehicle == null) {
            return new ResponseEntity<>("Missing param value", HttpStatus.BAD_REQUEST);
        }
        if (timestamp == null) {
            timestamp = Instant.now().getEpochSecond();
        }
        city = city.toUpperCase();
        vehicle = vehicle.toUpperCase();

        City enumCity;
        Vehicle enumVehicle;

        try {
            enumCity = City.valueOf(city);
            enumVehicle = Vehicle.valueOf(vehicle);

            Optional<Long> lastExecution = Cron.getLastExecutionUnixSeconds(timestamp);
            if (lastExecution.isEmpty()) {
                return new ResponseEntity<>("No records here", HttpStatus.OK);
            }
            Optional<WeatherConditions> wcFromDB = weatherConditionsRepository.findOneAtTimestamp(
                    lastExecution.get(),
                    enumCity.wmoCode
            );
            if (wcFromDB.isEmpty()) {
                return new ResponseEntity<>("Record doesn't exist", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            WeatherConditions wc = wcFromDB.get();

            int totalFee = FeeData.baseFee;
            totalFee += FeeData.cityFees.get(enumCity);
            totalFee += FeeData.vehicleFees.get(enumVehicle);

            if (enumVehicle == Vehicle.BIKE || enumVehicle == Vehicle.SCOOTER) {
                float temperature = wc.getAirTemperature();
                String phenomenon = wc.getWeatherPhenomenon();

                // Air Temperature Extra Fee
                if (temperature < -10F) {
                    totalFee += 100;
                } else if (temperature < 0F) {
                    totalFee += 50;
                }

                // Weather Phenomenon Extra Fee
                for (String p : FeeData.phenomenaSeverity.keySet()) {
                    if (phenomenon.contains(p)) {
                        int severity = FeeData.phenomenaSeverity.get(p);
                        if (severity == 1) {
                            totalFee += 50;
                        } else if (severity == 2) {
                            totalFee += 100;
                        } else {
                            return forbiddenVehicleResponse;
                        }
                        break;
                    }
                }
            }

            // Wind Speed Extra Fee
            if (enumVehicle == Vehicle.BIKE) {
                float windSpeed = wc.getWindSpeed();
                if (windSpeed > 20F) {
                    return forbiddenVehicleResponse;
                } else if (windSpeed > 10F) {
                    totalFee += 50;
                }
            }

            return new ResponseEntity<>(String.format("%.2f", (float) totalFee / 100), HttpStatus.OK);
        } catch (IllegalArgumentException error) {
            return new ResponseEntity<>("Invalid param value", HttpStatus.BAD_REQUEST);
        }

    }
}
