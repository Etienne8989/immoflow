package com.immoflow.immoflow.services;

import com.immoflow.immoflow.resource.PropertyData;
import com.immoflow.immoflow.resource.SimpleProxy;
import com.immoflow.immoflow.useragent.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.immoflow.immoflow.selenium.SeleniumUtils.*;

@Slf4j
@Service
public class PropertyParserManager {

    public List<PropertyData> getPropertyData(List<SimpleProxy> workingProxies, List<UserAgent> userAgentList) {

        //init web-driver
        String proxy = workingProxies.get(0).getHost() + ":" + workingProxies.get(0).getPort();
        log.info("a fresh proxy will be set {}", proxy);
        WebDriver          webDriver = initWebDriver(proxy, userAgentList.get(0).getUserAgent());
        JavascriptExecutor js        = (JavascriptExecutor) webDriver;

        List<PropertyData> propertyDataList = null;
        try {
            propertyDataList = startScraping(webDriver, js);
        } catch (WebDriverException  e) {
            log.debug(e.getClass().getName() + " : " + e.getMessage(), e);
            log.info("quit driver and start retry");
            webDriver.quit();
            sleep(2000);
            log.info("the proxy following proxy was not working or to slow. The proxy will be discarded {}:{}", workingProxies.get(0).getHost(), workingProxies.get(0).getPort());
            workingProxies.remove(0);
            log.info("the following useragent will be discarded {}", userAgentList.get(0));
            userAgentList.remove(0);
            getPropertyData(workingProxies, userAgentList);
        } finally {
            log.info("finally block invoced");
            sleepRandomTime(5000, 7000);
            webDriver.quit();
        }

        return propertyDataList;

    }


    private List<PropertyData> startScraping(WebDriver webDriver, JavascriptExecutor js) {

        //                int archivePageCount = parseAchivePageCount(webDriver, js);

        //start page
        //        scrapeFromStartPageTillCitySerch(webDriver, "düsseldorf");

        //archive page
        log.info("start accessing the web site");
        webDriver.get("https://www.immobilienscout24.de/Suche/de/nordrhein-westfalen/duesseldorf/wohnung-mieten?enteredFrom=one_step_search");
        log.info("");
        waitUntilElementIsVisible(webDriver, By.xpath("//*[@id=\"is24-de\"]/div[1]/div/div[1]/div/div/header/div/div[1]/a/img[2]"));
        sleepRandomTime(7000, 11000);
        List<WebElement> propertyCards = webDriver.findElements(By.className("result-list__listing"));
        List<WebElement> propertyCardTitlesWithoutCommercials = propertyCards
                .stream()
                .filter(p -> !p.getAttribute("class").contains("result-list__listing--xl"))
                .map(p -> p.findElement(By.className("result-list-entry__brand-title")))
                .collect(Collectors.toList());
        propertyCardTitlesWithoutCommercials.forEach(p -> log.info(p.toString()));
        List<PropertyData> propertyDataList = new ArrayList<>();

        propertyCardTitlesWithoutCommercials.forEach(p -> {

            sleepRandomTime(11000, 15000);

            scrollDownTillElementIsVisible((JavascriptExecutor) webDriver, p,100);

            sleepRandomTime(11000, 15000);
            log.info("the button from the property card will be clicked");
            p.click();
            sleepRandomTime(8000, 10000);

            //wait until immoscout banner appears
            waitUntilElementIsVisible(webDriver, By.xpath("//*[@id=\"is24-de\"]/div[2]/div[2]/div/header/div/div[1]/a/img[2]"));
            sleepRandomTime(5000, 7000);
            Document            page                = transformWebDriverContentToJsoupDocument(webDriver);
            PropertyParserJsoup propertyParserJsoup = new PropertyParserJsoup();
            PropertyData        propertyData        = propertyParserJsoup.scrapeData(page);
            log.info("the property data with the following title has been scraped : " + propertyData.getTitle());
            propertyDataList.add(propertyData);
            sleepRandomTime(3000, 6000);
            webDriver.navigate().back();
            sleepRandomTime(7000, 1000);
        });
        propertyDataList.forEach(p -> log.info(p.getTitle()));


        //property page


        //                        log.info("the current state of page count from immoscout is: " + archivePageCount);
        //
        //                        List<PropertyData> propertyDataList = new ArrayList<>();
        //                        for (int pageNumber = 1; pageNumber < archivePageCount; pageNumber++) {
        //                            waitUntilElementIsVisible(webDriver,By.xpath("//*[@id=\"is24-de\"]/div[1]/div/div[1]/div/div/header/div/div[1]/a/img[2]"));
        //                            sleepRandomTime(7000, 9000);
        //                            List<String> singlePageUrls = parseSinglePageLinks(webDriver, js, pageNumber);
        //                            if (singlePageUrls != null) {
        //                                for (String singlePageUrl : singlePageUrls) {
        //                                    sleepRandomTime(7000, 9000);
        //                                    PropertyData propertyData = parseSinglePage(webDriver, js, singlePageUrl);
        //                                    log.info("new data has been parsed: {}", propertyData);
        //                                    propertyDataList.add(propertyData);
        //                                }
        //                            }
        //                        }

        return null;
    }




    private void scrapeFromStartPageTillCitySerch(WebDriver webDriver, String city) {
        webDriver.get("https://www.immobilienscout24.de/");

        passCookieAcceptanceWindow(webDriver);

        waitUntilElementIsVisible(webDriver, By.xpath("//*[@id=\"oss-location\"]"));
        WebElement element = webDriver.findElement(By.xpath("//*[@id=\"oss-location\"]"));
        sleepRandomTime(1000, 2000);
        log.info("press enter");
        element.sendKeys(Keys.ENTER);
        sleepRandomTime(1000, 2000);
        log.info("run type");
        element.sendKeys(city);
        sleepRandomTime(5000, 7000);
        log.info("press enter");
        webDriver.findElement(By.xpath("//*[@id=\"oss-form\"]/article/div/div[3]/button")).click();
        sleepRandomTime(1000, 2000);
        webDriver.findElement(By.xpath("//*[@id=\"oss-form\"]/article/div/div[3]/button")).click();
        sleepRandomTime(20000, 25000);

        log.info("done");
    }

    private void passCookieAcceptanceWindow(WebDriver webDriver) {
        log.info("wait until cookie accept is visible or timeout");
        boolean isVisibilityTimeout = false;
        try {
            waitUntilElementIsVisible(webDriver, By.xpath("//*[@id=\"gdpr-consent-tool-wrapper\"]"), 15);
        } catch (TimeoutException e) {
            log.info("timeout has benn occurred. the cookie window won't be accepted");
            isVisibilityTimeout = true;
        }

        sleepRandomTime(5000, 7000);

        if (!isVisibilityTimeout) {
            String           parentWindowHandler = webDriver.getWindowHandle(); // Store your parent window
            String           subWindowHandler    = null;
            Set<String>      handles             = webDriver.getWindowHandles(); // get all window handles
            Iterator<String> iterator            = handles.iterator();


            while (iterator.hasNext()) {
                log.info("iterate windows");
                subWindowHandler = iterator.next();
            }

            log.info("switch to child window");
            webDriver.switchTo().window(subWindowHandler); // switch to popup window

            log.info("wait until manageSettings is visible");
            waitUntilElementIsVisible(webDriver, By.xpath("//*[@id=\"manageSettings\"]/span/div"));
            sleepRandomTime(1000, 2000);
            webDriver.findElement(By.xpath("//*[@id=\"manageSettings\"]/span/div")).click();

            log.info("wait until saveAndExit accept is visible");
            waitUntilElementIsVisible(webDriver, By.xpath("//*[@id=\"saveAndExit\"]/span/div"));
            sleepRandomTime(1000, 2000);
            webDriver.findElement(By.xpath("//*[@id=\"saveAndExit\"]/span/div")).click();

            sleepRandomTime(1000, 2000);
            // Now you are in the popup window, perform necessary actions here
            webDriver.switchTo().window(parentWindowHandler);  // switch back to parent window

        }
    }





    private int parseAchivePageCount(WebDriver webDriver) {
        sleepRandomTime(4, 7);
        String firstArchivePage = "https://www.immobilienscout24.de/Suche/de/nordrhein-westfalen/duesseldorf/wohnung-mieten?pagenumber=1";
        webDriver.get(firstArchivePage);
        Document page                 = transformWebDriverContentToJsoupDocument(webDriver);
        Element  archivePageAriaLabel = page.getElementsByAttributeValue("aria-label", "Seitenauswahl").first();
        int      archivePageCount     = archivePageAriaLabel.childrenSize();
        return archivePageCount;
    }

    private PropertyData parseSinglePage(WebDriver webDriver, String singlePageUrl) {
        sleepRandomTime(2, 6);
        webDriver.get("https://www.immobilienscout24.de" + singlePageUrl);
        Document            page                = transformWebDriverContentToJsoupDocument(webDriver);
        PropertyParserJsoup propertyParserJsoup = new PropertyParserJsoup();
        return propertyParserJsoup.scrapeData(page);
    }


    private List<String> parseSinglePageLinks(WebDriver webDriver, int pageNumber) {

        sleepRandomTime(4, 7);
        String currentArchivePage = "https://www.immobilienscout24.de/Suche/de/nordrhein-westfalen/duesseldorf/wohnung-mieten?pagenumber=" + pageNumber;
        webDriver.get(currentArchivePage);
        Document page = transformWebDriverContentToJsoupDocument(webDriver);

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
}
