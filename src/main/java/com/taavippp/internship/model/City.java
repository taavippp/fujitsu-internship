package com.taavippp.internship.model;

/**
 * Enum of the weather stations that this application uses.
 * <br/>
 * Key - weather station name
 * <br/>
 * Value - weather station WMO code (int)
 */
public enum City {
    TALLINN(26038),
    TARTU(26242),
    PÄRNU(41803);

    public final int wmoCode;

    private City(int wmoCode) {
        this.wmoCode = wmoCode;
    }
}