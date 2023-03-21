package com.taavippp.internship.repository;

import com.taavippp.internship.model.WeatherConditions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherConditionsRepository extends JpaRepository<WeatherConditions, Long> {}
