package com.taavippp.internship.model;

import org.jdom2.Element;

public class XMLWeatherStation implements IWeatherData {
    private int wmoCode = -1;
    private float airTemperature = 0;
    private float windSpeed = 0;
    private final String weatherPhenomenon;

    public XMLWeatherStation(Element element) {
        this.weatherPhenomenon = element.getChildText("phenomenon");
        try {
            this.wmoCode = Integer.parseInt(element.getChildText("wmocode"));
            this.airTemperature = Float.parseFloat(element.getChildText("airtemperature"));
            this.windSpeed = Float.parseFloat(element.getChildText("windspeed"));
        } catch (NumberFormatException ignored) {}
    }

    @Override
    public int getWMOCode() {
        return 0;
    }

    @Override
    public float getAirTemperature() {
        return airTemperature;
    }

    @Override
    public float getWindSpeed() {
        return windSpeed;
    }

    @Override
    public String getWeatherPhenomenon() {
        return weatherPhenomenon;
    }

    @Override
    public String toString() {
        return "XMLWeatherStation{" +
                "wmoCode=" + wmoCode +
                ", airTemperature=" + airTemperature +
                ", windSpeed=" + windSpeed +
                ", weatherPhenomenon='" + weatherPhenomenon + '\'' +
                '}';
    }
}
