package com.taavippp.internship.task;

import com.taavippp.internship.model.WeatherConditions;
import com.taavippp.internship.model.WeatherStations;
import com.taavippp.internship.model.XMLWeatherStation;
import com.taavippp.internship.repository.WeatherConditionsRepository;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;

@Component
public class WeatherDataRequest {

    private static final String URL = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
    private final String cronExpression = "0 15 * ? * *";
    private static final List<Integer> wmoCodes = List.of(
            WeatherStations.TALLINN_HARKU.wmoCode,
            WeatherStations.TARTU_TÕRAVERE.wmoCode,
            WeatherStations.PÄRNU.wmoCode
    );
    @Autowired
    WeatherConditionsRepository weatherConditionsRepository;

    // @Scheduled(fixedRate = 30000)
    @Scheduled(cron = cronExpression)
    public void request() throws URISyntaxException, IOException, InterruptedException, JDOMException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(new URI(URL))
                .timeout(Duration.of(10, SECONDS))
                .GET()
                .build();
        HttpResponse<String> res = HttpClient.newBuilder()
                .build()
                .send(
                        req,
                        HttpResponse.BodyHandlers.ofString()
                );
        SAXBuilder sax = new SAXBuilder();
        Document doc = sax.build(
                new StringReader(res.body())
        );
        Element root = doc.getRootElement();
        long timestamp = Long.parseLong(root.getAttributeValue("timestamp"));
        List<XMLWeatherStation> stations = root
                .getChildren()
                .stream()
                .map(XMLWeatherStation::new)
                .filter(station -> wmoCodes.contains(station.getWMOCode()))
                .toList();
        for (XMLWeatherStation station : stations) {
            weatherConditionsRepository.save(
                    new WeatherConditions(
                            station.getWMOCode(),
                            station.getAirTemperature(),
                            station.getWindSpeed(),
                            station.getWeatherPhenomenon(),
                            timestamp
                    )
            );
        }
        weatherConditionsRepository.flush();
    }

}
