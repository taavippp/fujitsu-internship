package com.taavippp.internship.repository;

import com.taavippp.internship.model.WeatherConditions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WeatherConditionsRepository extends JpaRepository<WeatherConditions, Long> {
    /**
     * Returns return the {@link WeatherConditions} between fromTimestamp and toTimestamp with matching WMO code.
     * FromTimestamp is incremented because BETWEEN is inclusive so there is a risk of returning both the current
     * and previous {@link WeatherConditions}, thus throwing an error.
    **/
    @Query(value = "SELECT wc " +
            "FROM WeatherConditions wc " +
            "WHERE wc.timestamp = :timestamp AND " +
            "wc.stationWMOCode = :wmocode " +
            "ORDER BY wc.timestamp DESC")
    public Optional<WeatherConditions> findOneAtTimestamp(
            @Param("timestamp") long timestamp,
            @Param("wmocode") int wmoCode
    );
}
