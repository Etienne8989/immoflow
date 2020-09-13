package com.immoflow.immoflow.services;

import com.immoflow.immoflow.resource.PropertyData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PropertyParserSelenium implements PropertyParser {

    @Override
    public PropertyData scrapeData(String basicUrl) {
        PropertyData propertyData = new PropertyData();

        WebDriver driver = initWebDriver();
        driver.get(basicUrl);

        extractAndSetTitle(propertyData, driver);

        String       zipCodeAndCityAndDistrict     = driver.findElement(By.className(".zip-region-and-country")).getText();
        List<String> zipCodeAndCityAndDistrictList = Arrays.asList(zipCodeAndCityAndDistrict.split("\\s"));
                    extractAndSetZipCode(propertyData, zipCodeAndCityAndDistrictList);
                    extractAndSetCity(propertyData, zipCodeAndCityAndDistrictList);
                    extractAndSetDistrict(propertyData, zipCodeAndCityAndDistrictList);

        //            Element street = zipCodeAndCityAndDistrict.previousElementSibling();
        //            extractAndSetStreet(propertyData, street);
        //            extraxtAndSetCostData(propertyData, page);
        //            extractAndSetAdditionalData(propertyData, page);

        return propertyData;
    }

    private void extractAndSetZipCode(PropertyData propertyData, List<String> zipCodeAndCityAndDistrictList) {
        if (zipCodeAndCityAndDistrictList.size() >= 1) {
            propertyData.setZipCode(zipCodeAndCityAndDistrictList.get(0));
        }
    }


    private void extractAndSetCity(PropertyData propertyData, List<String> zipCodeAndCityAndDistrictList) {
        if (zipCodeAndCityAndDistrictList.size() >= 2) {
            String cityPreState = zipCodeAndCityAndDistrictList.get(1);
            if (cityPreState.contains(",")) {
                propertyData.setCity(cityPreState.substring(0, cityPreState.indexOf(",")));
            } else {
                propertyData.setCity(cityPreState);
            }
        }
    }
    private void extractAndSetDistrict(PropertyData propertyData, List<String> zipCodeAndCityAndDistrictList) {
        if (zipCodeAndCityAndDistrictList.size() >= 3) {
            propertyData.setDistrict(zipCodeAndCityAndDistrictList.get(2));
        }
    }

    private void extractAndSetTitle(PropertyData propertyData, WebDriver driver) {
        WebElement element = driver.findElement(By.cssSelector(".criteriagroup #expose-title"));
        if (element != null) {
            propertyData.setTitle(element.getText());
        }
    }

    private WebDriver initWebDriver() {
        //driver init
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ehoven\\Documents\\webdrivers\\chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ehoven\\Documents\\webdrivers\\chromedriver.exe");
        ChromeOptions options             = new ChromeOptions();
        String        realUserAgentChrome = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36";
        options.addArguments(realUserAgentChrome);
        options.addArguments("--disable-extensions");
        options.addArguments("--profile-directory=Default");
        options.addArguments("--incognito");
        options.addArguments("--disable-plugins-discovery");
        options.addArguments("--start-maximized");
        //        options.addArguments("headless");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        return driver;
    }

}
