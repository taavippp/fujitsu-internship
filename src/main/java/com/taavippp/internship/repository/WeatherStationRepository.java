package com.taavippp.internship.repository;

import com.taavippp.internship.model.WeatherStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherStationRepository extends JpaRepository<WeatherStation, Long> {}
