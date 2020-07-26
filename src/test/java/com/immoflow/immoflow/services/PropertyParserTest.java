package com.immoflow.immoflow.services;

import com.immoflow.immoflow.resource.PropertyData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertyParserTest {

    @Test
    void getPropertyDataHappyFlow() {

        PropertyParser propertyParser = new PropertyParser();
        PropertyData   propertyData = propertyParser.getPropertyDataFromCard();

        //environment data
        assertEquals("**Nähe Seestern** Helle 3 Zi.-Whg. mit Einbauküche und Balkon, 40547 Düsseldorf-Lörick",propertyData.getTitle());
//        assertEquals("Deutschland",propertyData.getCountry()); //enum
//        assertEquals("Düsseldorf",propertyData.getCity());
//        assertEquals("Düsseldorf",propertyData.getZipCode());
//        assertEquals("40547",propertyData.getDistrict());
//        assertEquals("Wickrather Str. 35",propertyData.getStreet());
//        //costs
//        assertEquals("995 €",propertyData.getCostsCold());
//        assertEquals("+ 350 €",propertyData.getCostsAdditional());
//        assertEquals("in Nebenkosten enthalten",propertyData.getCostsHeating());
//        assertEquals("1.345 €",propertyData.getCostsTotal());
//        assertEquals("70 €",propertyData.getCostsParkingSpace());
//        assertEquals("3195",propertyData.getKaution());
//        //additional data
//        assertEquals("3",propertyData.getRooms());
//        assertEquals("1",propertyData.getRoomsBath());
//        assertEquals("AVAILABLE",propertyData.getParkingSpace()); //enum
//        assertEquals("xxxx",propertyData.getMoveInDate());
//        assertEquals("1973",propertyData.getConstructionYear());
//        assertEquals("zuletzt 2020",propertyData.getLastRenovation());
//        assertEquals("87 m²",propertyData.getAreaInM2());
//        assertEquals("Etagenwohnung",propertyData.getPropertyType());
//        assertEquals("5 von 8",propertyData.getFloor());
//        assertEquals("Nach Vereinbarung",propertyData.getPetsAllowed());
////        assertEquals("xxxx",propertyData.getKeller()); //enum
//        assertEquals("file:///C:/Users/ehoven/Documents/Projekte/immoflow/src/main/resources/scrapedata/immflow-scrape-page/__N%C3%A4he%20Seestern__%20Helle%203%20Zi.-Whg.%20mit%20Einbauk%C3%BCche%20und%20Balkon,%2040547%20D%C3%BCsseldorf-L%C3%B6rick.html#/",propertyData.getHttpLink());
    }

}
