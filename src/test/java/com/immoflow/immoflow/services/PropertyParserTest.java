package com.immoflow.immoflow.services;

import com.immoflow.immoflow.resource.Keller;
import com.immoflow.immoflow.resource.ParkingSpace;
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
        assertEquals("Deutschland",propertyData.getCountry()); //enum
        assertEquals("Düsseldorf",propertyData.getCity());
        assertEquals("40547",propertyData.getZipCode());
        assertEquals("Lörick",propertyData.getDistrict());
        assertEquals("Wickrather Str. 35",propertyData.getStreet());

//        //costs
        assertEquals("995 €",propertyData.getCostsCold());
        assertEquals("+ 350 €",propertyData.getCostsAdditional());
//        assertEquals("in Nebenkosten enthalten",propertyData.getCostsHeating());
        assertEquals("1.345 €",propertyData.getCostsTotal());
        assertEquals("70 €",propertyData.getCostsParkingSpace());
        assertEquals("3195",propertyData.getKaution());
//        //additional data
        assertEquals("Etagenwohnung",propertyData.getPropertyType());
        assertEquals("5 von 8",propertyData.getFloor());
        assertEquals("87 m²",propertyData.getAreaInM2());
        assertEquals("Ab sofort bezugsfrei",propertyData.getMoveInDate());
        assertEquals("3",propertyData.getRooms());
        assertEquals("1",propertyData.getRoomsBath());
        assertEquals(ParkingSpace.AVAILABLE, propertyData.getParkingSpace()); //enum
        assertEquals("Nach Vereinbarung",propertyData.getPetsAllowed());
        assertEquals("1973",propertyData.getConstructionYear());
        assertEquals("zuletzt 2020",propertyData.getLastRenovation());
        //tags
        assertEquals(Keller.AVAILABLE,propertyData.getKeller()); //enum
        assertEquals("C:\\Users\\ehoven\\Documents\\Projekte\\immoflow\\src\\main\\resources\\scrapedata\\immflow-scrape-page\\testpage.html",propertyData.getHttpLink());
    }

}
