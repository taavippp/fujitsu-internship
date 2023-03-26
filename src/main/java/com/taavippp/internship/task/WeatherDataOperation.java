package com.taavippp.internship.task;

import com.taavippp.internship.constant.Cron;
import com.taavippp.internship.model.WeatherConditions;
import com.taavippp.internship.model.City;
import com.taavippp.internship.model.XMLWeatherStation;
import com.taavippp.internship.repository.WeatherConditionsRepository;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.time.ZonedDateTime;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;

@Component
public class WeatherDataOperation {
    private static final Logger log = LoggerFactory.getLogger(WeatherDataOperation.class);
    private static final String URL = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
    private static final List<Integer> wmoCodes = List.of(
            City.TALLINN.wmoCode,
            City.TARTU.wmoCode,
            City.PÄRNU.wmoCode
    );
    @Autowired WeatherConditionsRepository weatherConditionsRepository;

    /**
     * Sends a HTTP request, parses the XML output and adds the fetched weather data to the database.
     * @throws URISyntaxException invalid URL, should not throw
     * @throws IOException issue with sending the request
     * @throws InterruptedException the thread sending the request was interrupted
     * @throws JDOMException invalid XML code, should not throw
     */
    @Scheduled(cron = Cron.expression)
    private void makeRequestAndAddResultsToDB() throws URISyntaxException, IOException, InterruptedException, JDOMException {
        long timestamp = ZonedDateTime.now().toEpochSecond();

        HttpRequest req = HttpRequest.newBuilder()
                .uri(new URI(URL))
                .timeout(Duration.of(10, SECONDS))
                .GET()
                .build();
        try {
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
        } catch (Error error) {
            log.error("An error occurred: " + error);
        }
    }

}
