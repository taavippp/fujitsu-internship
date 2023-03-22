package com.taavippp.internship.model;

public interface IWeatherData {
    int getWMOCode();
    float getAirTemperature();
    float getWindSpeed();
    String getWeatherPhenomenon();
}
