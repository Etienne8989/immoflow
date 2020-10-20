package com.immoflow.immoflow.selenium;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class SeleniumUtils {

    private static SeleniumSettings seleniumSettings = SeleniumSettings.getStaticSeleniumSettings();

    public static WebDriver initWebDriver(String proxyIp, String userAgent) {
        //driver init
        System.setProperty("webdriver.chrome.driver", seleniumSettings.getDriverpath());
        WebDriver driver = new ChromeDriver(setWebDriverSettings(proxyIp, userAgent));
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        return driver;
    }

    private static ChromeOptions setWebDriverSettings(String proxyIp, String userAgent) {
        ChromeOptions options = new ChromeOptions();
        String message = "The following settings for the selenium web driver have been set: \n";

        if (userAgent != null) {
            message = message + "--user-agent with user agent \n" + userAgent;
            options.addArguments("--user-agent=" + userAgent);
        }
        if (proxyIp != null) {
            message = message + "--proxy-server with proxy \n" + proxyIp;
            options.addArguments("--proxy-server=" + proxyIp);
        }
        if (seleniumSettings.isExtensionsDisabled()) {
            message = message + "--disable-extensions\n";
            options.addArguments("--disable-extensions");
        }

                options.addArguments("--profile-directory=Default");

        if (seleniumSettings.isIncognitoMode()) {
            message = message + "--incognito\n";
            options.addArguments("--incognito");
        }
        if (seleniumSettings.isPluginDiscoveryDisabled()) {
            message = message + "--disable-plugins-discovery\n";
            options.addArguments("--disable-plugins-discovery");
        }
        if (seleniumSettings.isHeadless()) {
            message = message + "headless\n";
            options.addArguments("headless");
        }
        if (seleniumSettings.isStartMaximized()) {
            message = message + "--start-maximized\n";
            options.addArguments("--start-maximized");
        }

        log.info(message);
        return options;
    }

}
