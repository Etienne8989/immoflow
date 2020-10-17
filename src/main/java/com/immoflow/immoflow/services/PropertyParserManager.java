package com.immoflow.immoflow.services;

import com.immoflow.immoflow.resource.PropertyData;
import com.immoflow.immoflow.resource.SimpleProxy;
import com.immoflow.immoflow.resource.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
public class PropertyParserManager {

    public List<PropertyData> getPropertyData() {
        //get proxies
        ProxyParserJsoup  proxyParserJsoup = new ProxyParserJsoup();
        List<SimpleProxy> workingProxies   = proxyParserJsoup.scrapeProxies(ScrapeUtilsJsoup.buildProxyContext());
        Collections.shuffle(workingProxies);
        //get useragents
        UserAgentFileParser userAgentFileParser = new UserAgentFileParser();
        List<UserAgent>     userAgentList       = userAgentFileParser.getUserAgentList();
        Collections.shuffle(userAgentList);

        //init web-driver
        String             proxy     = workingProxies.get(0).getHost() + ":" + workingProxies.get(0).getPort();
        WebDriver          webDriver = SeleniumUtils.initWebDriver(proxy, userAgentList.get(0).getUserAgent());
        JavascriptExecutor js        = (JavascriptExecutor) webDriver;

        int archivePageCount = parseAchivePageCount(webDriver, js);

        log.info("the current state of page count from immoscout is: " + archivePageCount);
        List<PropertyData> propertyDataList = new ArrayList<>();
        for (int pageNumber = 1; pageNumber < archivePageCount; pageNumber++) {
            List<String> singlePageUrls = parseSinglePageLinks(webDriver, js, pageNumber);
            if (singlePageUrls != null) {
                for (String singlePageUrl : singlePageUrls) {
                    PropertyData propertyData = parseSinglePage(webDriver, js, singlePageUrl);
                    log.info("new data has been parsed: {}", propertyData);
                    propertyDataList.add(propertyData);
                }
            }
        }
        return propertyDataList;
    }

    private int parseAchivePageCount(WebDriver webDriver, JavascriptExecutor js) {
        sleepRandomTime(4, 7);
        String firstArchivePage = "https://www.immobilienscout24.de/Suche/de/nordrhein-westfalen/duesseldorf/wohnung-mieten?pagenumber=1";
        webDriver.get(firstArchivePage);
        Document page                 = transformwebDriverContentToJsoupDocument(js);
        Element  archivePageAriaLabel = page.getElementsByAttributeValue("aria-label", "Seitenauswahl").first();
        int      archivePageCount     = archivePageAriaLabel.childrenSize();
        return archivePageCount;
    }

    private PropertyData parseSinglePage(WebDriver webDriver, JavascriptExecutor js, String singlePageUrl) {
        sleepRandomTime(2, 6);
        webDriver.navigate().to("https://www.immobilienscout24.de" + singlePageUrl);
        Document            page                = transformwebDriverContentToJsoupDocument(js);
        PropertyParserJsoup propertyParserJsoup = new PropertyParserJsoup();
        return propertyParserJsoup.scrapeData(page);
    }


    private List<String> parseSinglePageLinks(WebDriver webDriver, JavascriptExecutor js, int pageNumber) {

        sleepRandomTime(4, 7);
        String currentArchivePage = "https://www.immobilienscout24.de/Suche/de/nordrhein-westfalen/duesseldorf/wohnung-mieten?pagenumber=" + pageNumber;
        webDriver.navigate().to(currentArchivePage);
        Document page = transformwebDriverContentToJsoupDocument(js);

        log.info("scrape page ++ " + pageNumber + " ++");
        if (page != null) {
            //todo rausfinden warum css selector für die seite wohungs urls nicht funktioniet
            Elements propertyUrls = page.select(".result-list-entry__brand-title-container a");

            List<String> filteredUrls = propertyUrls.stream().map(Element::text)
                    .filter(t -> t.startsWith("/search/"))
                    .collect(Collectors.toList());

            filteredUrls.forEach(log::info);
            return filteredUrls;
        }
        return null;
    }

    private void sleepRandomTime(int min, int max) {
        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
        try {
            Thread.sleep(randomNum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private Document transformwebDriverContentToJsoupDocument(JavascriptExecutor js) {
        String   content = (String) js.executeScript("return document.documentElement.outerHTML;");
        Document page    = Jsoup.parse(content);
        return page;
    }

}
