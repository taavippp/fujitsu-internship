package com.taavippp.internship.constant;

import com.taavippp.internship.model.City;
import com.taavippp.internship.model.Vehicle;

import java.util.Map;

public class FeeData {
    public static final int baseFee = 100;
    /**
     * Fees based on city choice.
     **/
    public static final Map<City, Integer> cityFees = Map.of(
            City.TALLINN, 200,
            City.TARTU, 150,
            City.PÄRNU, 100
    );
    /**
     * Additional fees depending on vehicle choice.
     **/
    public static final Map<Vehicle, Integer> vehicleFees = Map.of(
            Vehicle.CAR, 100,
            Vehicle.SCOOTER, 50,
            Vehicle.BIKE, 0
    );
    /**
     * For easy comparison, all the different abnormal weather conditions are rated by severity 1-3, 3 being the worst.
     **/
    public static final Map<String, Integer> phenomenaSeverity = Map.of(
            "rain", 1,
            "snow", 2,
            "sleet", 2,
            "glaze", 3,
            "hail", 3,
            "thunder", 3
    );
}
