package com.taavippp.internship.model;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Assuming the weather site always returns accurate XML means
 * there is a lot less validation to be done.
**/
@SpringBootTest
public class XMLWeatherStationTests {
    String failingXML1 = "<station></station>";
    String failingXML2 = "<station><wmocode></wmocode><airtemperature></airtemperature></station>";
    String failingXML3 = "<station><wmocode>44</wmocode><airtemperature></airtemperature><windspeed></windspeed></station>";
    String passingXML = "<station><hello></hello><phenomenon>Rainy</phenomenon><wmocode>123</wmocode><airtemperature>45</airtemperature><windspeed>2</windspeed></station>";

    private XMLWeatherStation getXMLWeatherStationFromXML(String xml) throws IOException, JDOMException {
        SAXBuilder sax = new SAXBuilder();
        Document doc = sax.build(
                new StringReader(xml)
        );
        Element root = doc.getRootElement();
        return new XMLWeatherStation(root);
    }

    @Test
    void XMLWithNoChildrenWillUseDefaultValues() throws IOException, JDOMException {
        XMLWeatherStation ws = getXMLWeatherStationFromXML(failingXML1);
        assertEquals(-1, ws.getWMOCode());
        assertEquals(0, ws.getAirTemperature());
        assertEquals(0, ws.getWindSpeed());
        assertNull(ws.getWeatherPhenomenon());
    }

    @Test
    void XMLWithSomeEmptyChildrenWillUseDefaultValues() throws IOException, JDOMException {
        XMLWeatherStation ws = getXMLWeatherStationFromXML(failingXML2);
        assertEquals(-1, ws.getWMOCode());
        assertEquals(0, ws.getAirTemperature());
        assertEquals(0, ws.getWindSpeed());
        assertNull(ws.getWeatherPhenomenon());
    }

    @Test
    void XMLWithSomeSetChildrenWillUseDefaultValues() throws IOException, JDOMException {
        XMLWeatherStation ws = getXMLWeatherStationFromXML(failingXML3);
        assertEquals(44, ws.getWMOCode());
        assertEquals(0, ws.getAirTemperature());
        assertEquals(0, ws.getWindSpeed());
        assertNull(ws.getWeatherPhenomenon());
    }

    @Test
    void XMLWithAllSetChildrenWillGetValuesCorrectly() throws IOException, JDOMException {
        XMLWeatherStation ws = getXMLWeatherStationFromXML(passingXML);
        assertEquals(123, ws.getWMOCode());
        assertEquals(45F, ws.getAirTemperature());
        assertEquals(2F, ws.getWindSpeed());
        assertEquals("Rainy", ws.getWeatherPhenomenon());
    }
}
