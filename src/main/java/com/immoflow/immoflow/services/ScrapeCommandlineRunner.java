package com.immoflow.immoflow.services;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScrapeCommandlineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        String                    mainUrl             = "https://www.immobilienscout24.de/Suche/de/nordrhein-westfalen/duesseldorf/wohnung-mieten?pagenumber=";
        final PropertyParserJsoup propertyParserJsoup = new PropertyParserJsoup();
        Document                  startPage           = propertyParserJsoup.connectAndGetPage(mainUrl + "1");
        if (startPage != null) {
            log.info("getting started");
            int pageCount = startPage.getElementsByAttributeValue("aria-label", "Seitenauswahl").first().childrenSize();
            log.info("page count: " + pageCount);
            for (int i = 0; i < pageCount; i++) {
                Document page = propertyParserJsoup.connectAndGetPage(mainUrl + i);
                log.info("scrape page ++ " + i + " ++");
                if (page != null) {

                    //todo rausfinden warum css selector für die seite wohungs urls nicht funktioniet
                    Elements propertyUrls = page.select(".result-list-entry__brand-title-container a");

                    List<String> filteredUrls = propertyUrls.stream().map(Element::text)
                            .filter(t -> t.startsWith("/search/"))
                            .collect(Collectors.toList());

                    filteredUrls.forEach(t -> System.out.println(t));

                    for (String url : filteredUrls) {
                        System.out.println(url);
                        System.out.println(propertyParserJsoup.scrapeData(url));
                    }

                }
            }
        }
    }

}
