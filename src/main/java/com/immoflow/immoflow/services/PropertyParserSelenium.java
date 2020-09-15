package com.immoflow.immoflow.services;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PropertyParserSelenium {


    public List<String> getAllPropertyHrefsFromArchiveSite(String basicUrl, String proxyIp, String userAgent) throws InterruptedException {
        WebDriver driver = initWebDriver(proxyIp, userAgent);
        driver.get(basicUrl);
        //        JavascriptExecutor js = (JavascriptExecutor) driver;
        //        List<WebElement> e = (List<WebElement>) js.executeScript("return document.getElementByXPath('//article[contains(@class,'result-list-entry') and not (contains(@class,'result-list-entry--xl'))]//a[contains(@class,'result-list-entry__brand-title-container ')]');");
        //
        //        Thread.sleep(10000);
        //
        //        List<WebElement> elements = driver.findElements(
        //                By.xpath("//article[contains(@class,'result-list-entry') and not (contains(@class,'result-list-entry--xl'))]//a[contains(@class,'result-list-entry__brand-title-container ')]"));
        //
        //
        //        elements.size();

        return null;

    }

    //
    //    @Override
    //    public PropertyData scrapeData(String basicUrl) {
    //        PropertyData propertyData = new PropertyData();
    //
    //
    //        driver.get(basicUrl);
    //
    //        extractAndSetTitle(propertyData, driver);
    //
    //        WebElement   zipCodeAndCityAndDistrict     = driver.findElement(By.className("zip-region-and-country"));
    //        List<String> zipCodeAndCityAndDistrictList = Arrays.asList(zipCodeAndCityAndDistrict.getText().split("\\s"));
    //        extractAndSetZipCode(propertyData, zipCodeAndCityAndDistrictList);
    //        extractAndSetCity(propertyData, zipCodeAndCityAndDistrictList);
    //        extractAndSetDistrict(propertyData, zipCodeAndCityAndDistrictList);
    //
    //        //        WebElement street = zipCodeAndCityAndDistrict.findElement(By.xpath("following-sibling::*"));
    //
    //
    //        List<WebElement> Adressdata = driver.findElement(By.className("address-block")).findElements(By.tagName("span"));
    //        extractAndSetStreet(propertyData, Adressdata.get(0));
    //
    //        extraxtAndSetCostData(propertyData);
    //        //            extractAndSetAdditionalData(propertyData, page);
    //
    //        return propertyData;
    //    }
    //
    //    private void extractAndSetStreet(PropertyData propertyData, WebElement street) {
    //        if (street != null) {
    //            if (street.getText().contains(",")) {
    //                propertyData.setStreet(street.getText().substring(0, street.getText().indexOf(",")));
    //            } else {
    //                propertyData.setStreet(street.getText());
    //            }
    //        }
    //    }
    //
    //    private void extractAndSetZipCode(PropertyData propertyData, List<String> zipCodeAndCityAndDistrictList) {
    //        if (zipCodeAndCityAndDistrictList.size() >= 1) {
    //            propertyData.setZipCode(zipCodeAndCityAndDistrictList.get(0));
    //        }
    //    }
    //
    //
    //    private void extractAndSetCity(PropertyData propertyData, List<String> zipCodeAndCityAndDistrictList) {
    //        if (zipCodeAndCityAndDistrictList.size() >= 2) {
    //            String cityPreState = zipCodeAndCityAndDistrictList.get(1);
    //            if (cityPreState.contains(",")) {
    //                propertyData.setCity(cityPreState.substring(0, cityPreState.indexOf(",")));
    //            } else {
    //                propertyData.setCity(cityPreState);
    //            }
    //        }
    //    }
    //
    //    private void extractAndSetDistrict(PropertyData propertyData, List<String> zipCodeAndCityAndDistrictList) {
    //        if (zipCodeAndCityAndDistrictList.size() >= 3) {
    //            propertyData.setDistrict(zipCodeAndCityAndDistrictList.get(2));
    //        }
    //    }
    //
    //    private void extractAndSetTitle(PropertyData propertyData, WebDriver driver) {
    //        WebElement element = driver.findElement(By.cssSelector(".criteriagroup #expose-title"));
    //        if (element != null) {
    //            propertyData.setTitle(element.getText());
    //        }
    //    }
    //
    //
    //    private void extractAndSetAdditionalData(PropertyData propertyData, Document page) {
    //        Element propertyType = page.selectFirst(".is24qa-typ");
    //        if (propertyType != null) {
    //            propertyData.setPropertyType(propertyType.text());
    //        }
    //        Element propertyFloor = page.selectFirst(".is24qa-etage");
    //        if (propertyType != null) {
    //            propertyData.setFloor(propertyFloor.text());
    //        }
    //        Element propertyAreaInM2 = page.selectFirst(".is24qa-wohnflaeche-ca");
    //        if (propertyType != null) {
    //            propertyData.setAreaInM2(propertyAreaInM2.text());
    //        }
    //        Element propertyMoveInDate = page.selectFirst(".is24qa-bezugsfrei-ab ");
    //        if (propertyType != null) {
    //            propertyData.setMoveInDate(propertyMoveInDate.text());
    //        }
    //        Element propertyRoomsCount = page.selectFirst(".is24qa-zimmer");
    //        if (propertyType != null) {
    //            propertyData.setRooms(propertyRoomsCount.text());
    //        }
    //        Element propertyBathRoomsCount = page.selectFirst(".is24qa-badezimmer");
    //        if (propertyType != null) {
    //            propertyData.setRoomsBath(propertyBathRoomsCount.text());
    //        }
    //        Element propertyParkingSpace = page.selectFirst(".is24qa-garage-stellplatz-label");
    //        if (propertyParkingSpace != null) {
    //            propertyData.setParkingSpace(ParkingSpace.AVAILABLE);
    //        }
    //        Element petsAllowed = page.selectFirst(".is24qa-haustiere");
    //        if (propertyType != null) {
    //            propertyData.setPetsAllowed(petsAllowed.text());
    //        }
    //        Element propertyConstructionYear = page.selectFirst(".is24qa-baujahr");
    //        if (propertyType != null) {
    //            propertyData.setConstructionYear(propertyConstructionYear.text());
    //        }
    //        Element propertyLastRenovation = page.selectFirst(".is24qa-modernisierung-sanierung");
    //        if (propertyType != null) {
    //            propertyData.setLastRenovation(propertyLastRenovation.text());
    //        }
    //        Element propertyKeller = page.selectFirst(".is24qa-keller-label");
    //        if (propertyKeller != null) {
    //            propertyData.setKeller(Keller.AVAILABLE);
    //        }
    //        propertyData.setHttpLink(page.baseUri());
    //    }
    //
    //    private void extraxtAndSetCostData(PropertyData propertyData) {
    //        driver.findElement(By. ("body")).;
    //        Element costData = page.getElementsByAttributeValue("data-qa", "is24qa-kosten-label").first().nextElementSibling();
    //
    //        String propertyCostCold = Optional.ofNullable(costData.getElementsByClass("is24qa-kaltmiete"))
    //                .map(Elements::first)
    //                .map(Element::text)
    //                .orElse(null);
    //        propertyData.setCostsCold(propertyCostCold);
    //
    //        String propertyCostAdditional = Optional.ofNullable(costData.getElementsByClass("is24qa-nebenkosten"))
    //                .map(Elements::first)
    //                .map(Element::text)
    //                .orElse(null);
    //        propertyData.setCostsAdditional(propertyCostAdditional);
    //
    //        String propertyCostsParkingSpace = Optional.ofNullable(costData.getElementsByClass("is24qa-miete-fuer-garagestellplatz"))
    //                .map(Elements::first)
    //                .map(Element::text)
    //                .orElse(null);
    //        propertyData.setCostsParkingSpace(propertyCostsParkingSpace);
    //
    //        String propertyCostTotal = Optional.ofNullable(costData.getElementsByClass("is24qa-gesamtmiete"))
    //                .map(Elements::first)
    //                .map(Element::text)
    //                .orElse(null);
    //        propertyData.setCostsTotal(propertyCostTotal);
    //
    //        String propertyKaution = Optional.ofNullable(costData.getElementsByClass("is24qa-kaution-o-genossenschaftsanteile"))
    //                .map(Elements::first)
    //                .map(Element::text)
    //                .orElse(null);
    //        propertyData.setKaution(propertyKaution);
    //    }
    //
    //    private void extractAndSetStreet(PropertyData propertyData, Element street) {
    //        if (street != null) {
    //            if (street.text().contains(",")) {
    //                propertyData.setStreet(street.text().substring(0, street.text().indexOf(",")));
    //            } else {
    //                propertyData.setStreet(street.text());
    //            }
    //        }
    //    }
    //
    //    private void extractAndSetTitle(PropertyData propertyData, Document page) {
    //        Element title = page.selectFirst(".criteriagroup #expose-title");
    //        if (title != null) {
    //            propertyData.setTitle(title.text());
    //        }
    //    }
    //
    //
    //    Document connectAndGetPage(String basicUrl) {
    //        Document page = null;
    //        try {
    //            if (basicUrl.startsWith("http")) {
    //                //connect to URL
    //                page = Jsoup.connect(
    //                        basicUrl)
    //                        .userAgent(USER_AGENT)
    //                        .get();
    //            } else {
    //                //connect to file
    //                File html = new File(basicUrl);
    //                page = Jsoup.parse(html, null);
    //            }
    //
    //        } catch (IOException ex) {
    //            log.debug("could not parse given website ", ex);
    //        }
    //        return page;
    //    }
    //
    //
    private WebDriver initWebDriver(String proxyIp, String userAgent) {
        //driver init
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ehoven\\Documents\\webdrivers\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--user-agent=" + userAgent);
        if (proxyIp != null) {
            options.addArguments("--proxy-server=" + proxyIp);
        }
        //        options.addArguments("--disable-extensions");
        //        options.addArguments("--profile-directory=Default");
        //        options.addArguments("--incognito");
        //        options.addArguments("--disable-plugins-discovery");
        //        options.addArguments("headless");
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        return driver;
    }

}
