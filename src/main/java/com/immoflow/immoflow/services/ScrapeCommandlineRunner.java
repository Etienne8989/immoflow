package com.immoflow.immoflow.services;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScrapeCommandlineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        String               mainUrl        = "https://www.immobilienscout24.de/Suche/de/nordrhein-westfalen/duesseldorf/wohnung-mieten?pagenumber=";
        final PropertyParser propertyParser = new PropertyParser();
        Document             startPage      = propertyParser.connectAndGetPage(mainUrl + "1");
        if (startPage != null) {
            System.out.println("getting started");
            int pageCount = startPage.getElementsByAttributeValue("aria-label", "Seitenauswahl").first().childrenSize();
            System.out.println("page count: " + pageCount);
            for (int i = 0; i < pageCount; i++) {
                Document page = propertyParser.connectAndGetPage(mainUrl + i);
                System.out.println("scrape page ++ " + i + " ++");
                if (page != null) {

                    //todo rausfinden warum css selector für die seite wohungs urls nicht funktioniet
                    Elements propertyUrls = page.select(".result-list-entry__brand-title-container a");

                    List<String> filteredUrls = propertyUrls.stream().map(Element::text)
                            .filter(t -> t.startsWith("/search/"))
                            .collect(Collectors.toList());

                    filteredUrls.forEach(t -> System.out.println(t));

                    for (String url : filteredUrls) {
                        System.out.println(url);
                        System.out.println(propertyParser.scrapeData(url));
                    }

                }
            }
        }
    }

}
