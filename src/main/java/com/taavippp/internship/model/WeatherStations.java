package com.taavippp.internship.model;

/**
 * Enum of the weather stations that this application uses.
 * <br/>
 * Key - weather station name
 * <br/>
 * Value - weather station WMO code (int)
 */
public enum WeatherStations {
    TALLINN_HARKU(26038),
    TARTU_TÕRAVERE(26242),
    PÄRNU(41803);

    public final int wmoCode;

    private WeatherStations(int wmoCode) {
        this.wmoCode = wmoCode;
    }
}