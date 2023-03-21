package com.taavippp.internship.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public record WeatherStation(
        @Id Long ID,
        String name,
        Integer WMOCode
) {}
