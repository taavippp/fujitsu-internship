package com.taavippp.internship.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public record WeatherConditions(
        @Id Long ID,
        Long weatherStationID,
        Byte airTemperature,
        Byte windSpeed,
        LocalDateTime timestamp
) {}
