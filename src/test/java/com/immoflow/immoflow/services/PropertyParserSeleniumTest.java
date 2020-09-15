package com.immoflow.immoflow.services;

import org.junit.jupiter.api.Test;

class PropertyParserSeleniumTest {

    // ggf auschecken für free proxies https://github.com/hyan15/scrapy-proxy-pool
    // seite um procxies zu kaufen https://www.hidemyass.com/de-de/index
    //video für proxies https://www.youtube.com/watch?v=090tLVr0l7s
    //video für user-agent https://www.youtube.com/watch?v=GOjuQ9IgSfI
    //Adresse für User-Agents -> https://pypi.org/project/scrapy-user-agents/

    private static final String BROWSER_INFO_URL = "https://www.whatismybrowser.com/detect/what-http-headers-is-my-browser-sending";
    private static final String IMMOSCOUT_PRODUCT_URL = "C:\\Users\\ehoven\\Documents\\Projekte\\immoflow\\src\\main\\resources\\scrapedata\\immflow-scrape-page\\testpage.html";
    //private static final String ARCHIVE_URL = "C:\\Users\\ehoven\\Documents\\Projekte\\immoflow\\src\\main\\resources\\scrapedata\\achrive-page\\archive-page.html";
    private static final String IMMOSCOUT_ARCHIVE_URL = "https://www.immobilienscout24.de/Suche/de/nordrhein-westfalen/duesseldorf/wohnung-mieten?enteredFrom=one_step_search";

    public static final String REAL_USER_AGENT        = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36";
    public static final String GOOGLE_BOT_USER_AGENT1 = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
    public static final String GOOGLE_BOT_USER_AGENT2 = "Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko; compatible; Googlebot/2.1; +http://www.google.com/bot.html) Chrome/W.X.Y.Z‡ Safari/537.36";
    //wird eher selten von google verwendet
    public static final String GOOGLE_BOT_USER_AGENT3 = "Googlebot/2.1 (+http://www.google.com/bot.html)";

    @Test
    void getAllPropertyHrefsFromArchiveSite() throws InterruptedException {
        PropertyParserSelenium propertyParserSelenium = new PropertyParserSelenium();
        //        propertyParserSelenium.getAllPropertyHrefsFromArchiveSite(ARCHIVE_URL, "213.136.89.121:80");
        propertyParserSelenium.getAllPropertyHrefsFromArchiveSite(BROWSER_INFO_URL, null, GOOGLE_BOT_USER_AGENT1);
    }

    //
    //    @Test
    //    void scrapeData() {
    //        PropertyParserSelenium propertyParserSelenium = new PropertyParserSelenium();
    //        PropertyData           propertyData           = propertyParserSelenium.scrapeData(PRODUCT_URL);
    //
    //        //environment data
    //        assertEquals("**Nähe Seestern** Helle 3 Zi.-Whg. mit Einbauküche und Balkon, 40547 Düsseldorf-Lörick", propertyData.getTitle());
    //        assertEquals("Deutschland", propertyData.getCountry()); //enum
    //        assertEquals("Düsseldorf", propertyData.getCity());
    //        assertEquals("40547", propertyData.getZipCode());
    //        assertEquals("Lörick", propertyData.getDistrict());
    //        assertEquals("Wickrather Str. 35", propertyData.getStreet());
    ////        //costs
    //        assertEquals("995 €", propertyData.getCostsCold());
    //        assertEquals("+ 350 €", propertyData.getCostsAdditional());
    //        //        assertEquals("in Nebenkosten enthalten",propertyData.getCostsHeating());
    //        assertEquals("1.345 €", propertyData.getCostsTotal());
    //        assertEquals("70 €", propertyData.getCostsParkingSpace());
    //        assertEquals("3195", propertyData.getKaution());
    //        //additional data
    //        assertEquals("Etagenwohnung", propertyData.getPropertyType());
    //        assertEquals("5 von 8", propertyData.getFloor());
    //        assertEquals("87 m²", propertyData.getAreaInM2());
    //        assertEquals("Ab sofort bezugsfrei", propertyData.getMoveInDate());
    //        assertEquals("3", propertyData.getRooms());
    //        assertEquals("1", propertyData.getRoomsBath());
    //        assertEquals(ParkingSpace.AVAILABLE, propertyData.getParkingSpace()); //enum
    //        assertEquals("Nach Vereinbarung", propertyData.getPetsAllowed());
    //        assertEquals("1973", propertyData.getConstructionYear());
    //        assertEquals("zuletzt 2020", propertyData.getLastRenovation());
    //        //tags
    //        assertEquals(Keller.AVAILABLE, propertyData.getKeller()); //enum
    //        assertEquals("C:\\Users\\ehoven\\Documents\\Projekte\\immoflow\\src\\main\\resources\\scrapedata\\immflow-scrape-page\\testpage.html", propertyData.getHttpLink());
}


