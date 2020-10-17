package com.immoflow.immoflow.services;

import com.immoflow.immoflow.resource.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/*NOTE: Jsoup is not able to execute java script. therefore the jsoup property parser is currently not working*/

@Slf4j
@AllArgsConstructor
public class PropertyParserJsoup implements PropertyParser {

    UserAgentParser<UserAgent> userAgentParser;
    ProxyParser<SimpleProxy>   proxyParser;
    ScrapeUtilsJsoup scrapeUtilsJsoup;

    @Override
    public PropertyData scrapeData(String basicUrl) {
        PropertyData propertyData = new PropertyData();

        List<UserAgent> userAgentList = userAgentParser.getUserAgentList();
        Collections.shuffle(userAgentList);

        ProxyContext proxyContext = new ProxyContext();
        proxyContext.setSslProxies(true);

        List<SimpleProxy> workingProxies = proxyParser.scrapeProxies(proxyContext);
        Collections.shuffle(workingProxies);

        Document page =getPageByContext(basicUrl,userAgentList,workingProxies);

        if (page != null) {
            extractAndSetTitle(propertyData, page);

            Element      zipCodeAndCityAndDistrict     = page.selectFirst(".zip-region-and-country");
            List<String> zipCodeAndCityAndDistrictList = Arrays.asList(zipCodeAndCityAndDistrict.text().split("\\s"));
            extractAndSetZipCode(propertyData, zipCodeAndCityAndDistrictList);
            extractAndSetCity(propertyData, zipCodeAndCityAndDistrictList);
            extractAndSetDistrict(propertyData, zipCodeAndCityAndDistrictList);

            Element street = zipCodeAndCityAndDistrict.previousElementSibling();
            extractAndSetStreet(propertyData, street);

            extraxtAndSetCostData(propertyData, page);

            extractAndSetAdditionalData(propertyData, page);
        }
        return propertyData;
    }

    private Document getPageByContext(String basicUrl, List<UserAgent> userAgentList, List<SimpleProxy> workingProxies) {
        Document page = null;
        if (!workingProxies.isEmpty() && !userAgentList.isEmpty()) {
            page = scrapeUtilsJsoup.connectAndGetPage(basicUrl, workingProxies.get(0), userAgentList.get(0));
        } else if (!userAgentList.isEmpty()) {
            page = scrapeUtilsJsoup.connectAndGetPage(basicUrl, userAgentList.get(0));
        } else {
            page = scrapeUtilsJsoup.connectAndGetPage(basicUrl);
        }
        return page;
    }



    private void extractAndSetAdditionalData(PropertyData propertyData, Document page) {
        Element propertyType = page.selectFirst(".is24qa-typ");
        if (propertyType != null) {
            propertyData.setPropertyType(propertyType.text());
        }
        Element propertyFloor = page.selectFirst(".is24qa-etage");
        if (propertyType != null) {
            propertyData.setFloor(propertyFloor.text());
        }
        Element propertyAreaInM2 = page.selectFirst(".is24qa-wohnflaeche-ca");
        if (propertyType != null) {
            propertyData.setAreaInM2(propertyAreaInM2.text());
        }
        Element propertyMoveInDate = page.selectFirst(".is24qa-bezugsfrei-ab ");
        if (propertyType != null) {
            propertyData.setMoveInDate(propertyMoveInDate.text());
        }
        Element propertyRoomsCount = page.selectFirst(".is24qa-zimmer");
        if (propertyType != null) {
            propertyData.setRooms(propertyRoomsCount.text());
        }
        Element propertyBathRoomsCount = page.selectFirst(".is24qa-badezimmer");
        if (propertyType != null) {
            propertyData.setRoomsBath(propertyBathRoomsCount.text());
        }
        Element propertyParkingSpace = page.selectFirst(".is24qa-garage-stellplatz-label");
        if (propertyParkingSpace != null) {
            propertyData.setParkingSpace(ParkingSpace.AVAILABLE);
        }
        Element petsAllowed = page.selectFirst(".is24qa-haustiere");
        if (propertyType != null) {
            propertyData.setPetsAllowed(petsAllowed.text());
        }
        Element propertyConstructionYear = page.selectFirst(".is24qa-baujahr");
        if (propertyType != null) {
            propertyData.setConstructionYear(propertyConstructionYear.text());
        }
        Element propertyLastRenovation = page.selectFirst(".is24qa-modernisierung-sanierung");
        if (propertyType != null) {
            propertyData.setLastRenovation(propertyLastRenovation.text());
        }
        Element propertyKeller = page.selectFirst(".is24qa-keller-label");
        if (propertyKeller != null) {
            propertyData.setKeller(Keller.AVAILABLE);
        }
        propertyData.setHttpLink(page.baseUri());
    }

    private void extraxtAndSetCostData(PropertyData propertyData, Document page) {
        Element costData = page.getElementsByAttributeValue("data-qa", "is24qa-kosten-label").first().nextElementSibling();

        String propertyCostCold = Optional.ofNullable(costData.getElementsByClass("is24qa-kaltmiete"))
                .map(Elements::first)
                .map(Element::text)
                .orElse(null);
        propertyData.setCostsCold(propertyCostCold);

        String propertyCostAdditional = Optional.ofNullable(costData.getElementsByClass("is24qa-nebenkosten"))
                .map(Elements::first)
                .map(Element::text)
                .orElse(null);
        propertyData.setCostsAdditional(propertyCostAdditional);

        String propertyCostsParkingSpace = Optional.ofNullable(costData.getElementsByClass("is24qa-miete-fuer-garagestellplatz"))
                .map(Elements::first)
                .map(Element::text)
                .orElse(null);
        propertyData.setCostsParkingSpace(propertyCostsParkingSpace);

        String propertyCostTotal = Optional.ofNullable(costData.getElementsByClass("is24qa-gesamtmiete"))
                .map(Elements::first)
                .map(Element::text)
                .orElse(null);
        propertyData.setCostsTotal(propertyCostTotal);

        String propertyKaution = Optional.ofNullable(costData.getElementsByClass("is24qa-kaution-o-genossenschaftsanteile"))
                .map(Elements::first)
                .map(Element::text)
                .orElse(null);
        propertyData.setKaution(propertyKaution);
    }

    private void extractAndSetStreet(PropertyData propertyData, Element street) {
        if (street != null) {
            if (street.text().contains(",")) {
                propertyData.setStreet(street.text().substring(0, street.text().indexOf(",")));
            } else {
                propertyData.setStreet(street.text());
            }
        }
    }

    private void extractAndSetTitle(PropertyData propertyData, Document page) {
        Element title = page.selectFirst(".criteriagroup #expose-title");
        if (title != null) {
            propertyData.setTitle(title.text());
        }
    }

    private void extractAndSetDistrict(PropertyData propertyData, List<String> zipCodeAndCityAndDistrictList) {
        if (zipCodeAndCityAndDistrictList.size() >= 3) {
            propertyData.setDistrict(zipCodeAndCityAndDistrictList.get(2));
        }
    }

    private void extractAndSetCity(PropertyData propertyData, List<String> zipCodeAndCityAndDistrictList) {
        if (zipCodeAndCityAndDistrictList.size() >= 2) {
            String cityPreState = zipCodeAndCityAndDistrictList.get(1);
            if (cityPreState.contains(",")) {
                propertyData.setCity(cityPreState.substring(0, cityPreState.indexOf(",")));
            } else {
                propertyData.setCity(cityPreState);
            }
        }
    }

    private void extractAndSetZipCode(PropertyData propertyData, List<String> zipCodeAndCityAndDistrictList) {
        if (zipCodeAndCityAndDistrictList.size() >= 1) {
            propertyData.setZipCode(zipCodeAndCityAndDistrictList.get(0));
        }
    }

    public PropertyParserJsoup(UserAgentParser<UserAgent> userAgentParser, ProxyParser<SimpleProxy> proxyParser) {
        this.userAgentParser = userAgentParser;
        this.proxyParser = proxyParser;
    }

}
