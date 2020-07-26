package com.immoflow.immoflow.services;

import com.immoflow.immoflow.resource.PropertyData;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;

@Slf4j
public class PropertyParser {

    private static final String BASIC_URL  = "C:\\Users\\ehoven\\Documents\\Projekte\\immoflow\\src\\main\\resources\\scrapedata\\immflow-scrape-page\\testpage.html";
    private static final String FILE_FOR_LOCAL_TEST  = "C:\\Users\\ehoven\\Documents\\Projekte\\immoflow\\src\\main\\resources\\scrapedata\\immflow-scrape-page\\testpage.html";
    private static final String USER_AGENT = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";

    public PropertyData getPropertyDataFromCard() {

        PropertyData propertyData = new PropertyData();

        Document page    = connectAndGetSinglePage();
        Element  element = page.selectFirst(".criteriagroup #expose-title");

        propertyData.setTitle(element.text());

        return propertyData;
    }

    private Document connectAndGetSinglePage() {
        Document page = null;
        try {
            //connect to file
            File     html   = new File(FILE_FOR_LOCAL_TEST);
            page  = Jsoup.parse(html, null);
            //connect to URL
//            page = Jsoup.connect(
//                    BASIC_URL)
//                    .userAgent(USER_AGENT)
//                    .get();
        } catch (IOException ex) {
            log.debug("could not parse given website ", ex);
        }
        return page;
    }

}
