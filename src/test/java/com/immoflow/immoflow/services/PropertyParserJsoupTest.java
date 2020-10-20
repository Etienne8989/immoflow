package com.immoflow.immoflow.services;

import com.immoflow.immoflow.TestUtils;
import com.immoflow.immoflow.proxies.ProxyParserJsoup;
import com.immoflow.immoflow.resource.*;
import com.immoflow.immoflow.useragent.UserAgent;
import com.immoflow.immoflow.useragent.UserAgentFileParser;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PropertyParserJsoupTest {

    private static final String File = "C:\\Users\\ehoven\\Documents\\Projekte\\immoflow\\src\\main\\resources\\scrapedata\\immflow-scrape-page\\testpage.html";
    private static final String URL  = "https://www.immobilienscout24.de/";

    @Test
    @Disabled
    void getPropertyDataByFileHappyFlow() {

        PropertyParserJsoup propertyParser = new PropertyParserJsoup(new UserAgentFileParser(), new ProxyParserJsoup());
        PropertyData   propertyData   = propertyParser.scrapeData(File);

        //environment data
        assertEquals("**Nähe Seestern** Helle 3 Zi.-Whg. mit Einbauküche und Balkon, 40547 Düsseldorf-Lörick", propertyData.getTitle());
        assertEquals("Deutschland", propertyData.getCountry()); //enum
        assertEquals("Düsseldorf", propertyData.getCity());
        assertEquals("40547", propertyData.getZipCode());
        assertEquals("Lörick", propertyData.getDistrict());
        assertEquals("Wickrather Str. 35", propertyData.getStreet());
        //costs
        assertEquals("995 €", propertyData.getCostsCold());
        assertEquals("+ 350 €", propertyData.getCostsAdditional());
        //assertEquals("in Nebenkosten enthalten",propertyData.getCostsHeating());
        assertEquals("1.345 €", propertyData.getCostsTotal());
        assertEquals("70 €", propertyData.getCostsParkingSpace());
        assertEquals("3195", propertyData.getKaution());
        //additional data
        assertEquals("Etagenwohnung", propertyData.getPropertyType());
        assertEquals("5 von 8", propertyData.getFloor());
        assertEquals("87 m²", propertyData.getAreaInM2());
        assertEquals("Ab sofort bezugsfrei", propertyData.getMoveInDate());
        assertEquals("3", propertyData.getRooms());
        assertEquals("1", propertyData.getRoomsBath());
        assertEquals(ParkingSpace.AVAILABLE, propertyData.getParkingSpace()); //enum
        assertEquals("Nach Vereinbarung", propertyData.getPetsAllowed());
        assertEquals("1973", propertyData.getConstructionYear());
        assertEquals("zuletzt 2020", propertyData.getLastRenovation());
        //tags
        assertEquals(Keller.AVAILABLE, propertyData.getKeller()); //enum
        assertEquals("C:\\Users\\ehoven\\Documents\\Projekte\\immoflow\\src\\main\\resources\\scrapedata\\immflow-scrape-page\\testpage.html", propertyData.getHttpLink());
    }

    @Test
    void getPropertyDataByURLHappyFlow() {

        ProxyParserJsoup proxyParserJsoup = new ProxyParserJsoup();
        TestUtils.setProxyProperties();
        List<SimpleProxy>   workingProxies      = proxyParserJsoup.scrapeProxies(TestUtils.buildProxyContext());
        UserAgentFileParser userAgentFileParser = new UserAgentFileParser();
        List<UserAgent>     userAgentList       = userAgentFileParser.getUserAgentList();
        ScrapeUtilsJsoup    scrapeUtilsJsoup    = new ScrapeUtilsJsoup();
        Document            document            = scrapeUtilsJsoup.connectAndGetPage(URL, workingProxies.get(0), userAgentList.get(0));
        System.out.println(document.body());
//        PropertyParser propertyParser = new PropertyParserJsoup(new UserAgentFileParser(), new ProxyParserJsoup());
//        PropertyData   propertyData   = propertyParser.scrapeData(URL);
    }

}
