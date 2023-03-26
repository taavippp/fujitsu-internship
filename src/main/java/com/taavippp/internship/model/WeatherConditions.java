package com.taavippp.internship.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * A class with different weather data that will is inserted into the database.
**/
@Entity
public class WeatherConditions implements IWeatherData {

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long ID;
        private int stationWMOCode;
        private float airTemperature;
        private float windSpeed;
        private String weatherPhenomenon;
        private long timestamp;

        // JPA uses this one
        protected WeatherConditions() {}

        public WeatherConditions(int stationWMOCode, float airTemperature, float windSpeed, String weatherPhenomenon, long timestamp) {
                this.stationWMOCode = stationWMOCode;
                this.airTemperature = airTemperature;
                this.windSpeed = windSpeed;
                this.weatherPhenomenon = weatherPhenomenon;
                this.timestamp = timestamp;
        }

        @Override
        public int getWMOCode() {
                return stationWMOCode;
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

        public long getTimestamp() {
                return timestamp;
        }

        @Override
        public String toString() {
                return "WeatherConditions{" +
                        "stationWMOCode=" + stationWMOCode +
                        ", airTemperature=" + airTemperature +
                        ", windSpeed=" + windSpeed +
                        ", timestamp=" + timestamp +
                        '}';
        }
}
