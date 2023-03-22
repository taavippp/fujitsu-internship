package com.taavippp.internship.controller;

import com.taavippp.internship.model.WeatherConditions;
import com.taavippp.internship.repository.WeatherConditionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MainController.path)
public class MainController {
    public static final String path = "/api";

    @Autowired
    WeatherConditionsRepository weatherConditionsRepository;

    @GetMapping()
    public ResponseEntity<String> getWeatherConditions(
    ) {
        StringBuilder sb = new StringBuilder();
        for (WeatherConditions wc : weatherConditionsRepository.findAll()) {
            sb.append(wc.toString()).append("<br/>");
        }
        return new ResponseEntity<String>(sb.toString(), HttpStatus.OK);
    }

}
