package com.immoflow.immoflow.services;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class SeleniumUtils {

    public static WebDriver initWebDriver(String proxyIp, String userAgent) {
        //driver init
        //        System.setProperty("webdriver.chrome.driver", "C:\\Users\\ehoven\\Documents\\webdrivers\\chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", "/Users/etho/projects-programming/immoflow/src/main/resources/web-driver/chromedriver");
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
