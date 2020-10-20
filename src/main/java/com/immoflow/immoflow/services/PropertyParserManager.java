package com.immoflow.immoflow.services;

import com.immoflow.immoflow.resource.PropertyData;
import com.immoflow.immoflow.resource.SimpleProxy;
import com.immoflow.immoflow.selenium.SeleniumUtils;
import com.immoflow.immoflow.useragent.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PropertyParserManager {

    public List<PropertyData> getPropertyData(List<SimpleProxy> workingProxies, List<UserAgent> userAgentList) {

        //init web-driver
        String proxy = workingProxies.get(0).getHost() + ":" + workingProxies.get(0).getPort();
        log.info("a fresh proxy will be set {}", proxy);
        WebDriver          webDriver = SeleniumUtils.initWebDriver(proxy, userAgentList.get(0).getUserAgent());
        JavascriptExecutor js        = (JavascriptExecutor) webDriver;

        List<PropertyData> propertyDataList = null;
        try {
            propertyDataList = startScraping(webDriver, js);
        } catch (TimeoutException e) {
            log.info("exception : " + e);
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


        //wait until page is fully loaded
        //        new WebDriverWait(webDriver, 130).until(driver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        ////        sleepRandomTime(9000, 15000);


        scrapeFromStartPageTillCitySerch(webDriver,  "düsseldorf");


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

    private void waitUntilElementIsVisible(WebDriver webDriver, By visibleElement) {
        WebDriverWait wait = new WebDriverWait(webDriver, 100);
        wait.until(ExpectedConditions.visibilityOfElementLocated(visibleElement));
        sleepRandomTime(3000, 5000);
    }

    private void waitUntilElementIsVisible(WebDriver webDriver, By visibleElement, int timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(webDriver, timeOutInSeconds);
        wait.until(ExpectedConditions.visibilityOfElementLocated(visibleElement));
        sleepRandomTime(3000, 5000);
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
        webDriver.get("https://www.immobilienscout24.de" + singlePageUrl);
        Document            page                = transformwebDriverContentToJsoupDocument(js);
        PropertyParserJsoup propertyParserJsoup = new PropertyParserJsoup();
        return propertyParserJsoup.scrapeData(page);
    }


    private List<String> parseSinglePageLinks(WebDriver webDriver, JavascriptExecutor js, int pageNumber) {

        sleepRandomTime(4, 7);
        String currentArchivePage = "https://www.immobilienscout24.de/Suche/de/nordrhein-westfalen/duesseldorf/wohnung-mieten?pagenumber=" + pageNumber;
        webDriver.get(currentArchivePage);
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

    private void sleep(int timeInMillis) {
        try {
            Thread.sleep(timeInMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sleepRandomTime(int minInMillis, int maxInMillis) {
        int randomNum = ThreadLocalRandom.current().nextInt(minInMillis, maxInMillis + 1);
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
