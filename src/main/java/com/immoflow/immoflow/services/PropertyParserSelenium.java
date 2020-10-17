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
    public PropertyData scrapeData(String basicUrl, String proxyIp, String userAgent) {
        PropertyData propertyData = new PropertyData();

        WebDriver driver = SeleniumUtils.initWebDriver(proxyIp, userAgent);
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

}
