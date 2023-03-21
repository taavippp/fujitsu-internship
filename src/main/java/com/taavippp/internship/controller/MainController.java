package com.taavippp.internship.controller;

import com.taavippp.internship.repository.WeatherConditionsRepository;
import com.taavippp.internship.repository.WeatherStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MainController.path)
public class MainController {
    public static final String path = "/api";

    @Autowired
    WeatherStationRepository weatherStationRepository;
    @Autowired
    WeatherConditionsRepository weatherConditionsRepository;

    @GetMapping()
    public ResponseEntity<String> helloWorld(
            @RequestParam(defaultValue = "world") String name
    ) {
        return new ResponseEntity<>("Hello " + name, HttpStatus.OK);
    }
}
