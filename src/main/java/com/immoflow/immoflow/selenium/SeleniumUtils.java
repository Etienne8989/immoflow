package com.immoflow.immoflow.selenium;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
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
            message = message + "--user-agent with user agent " + userAgent + "\n" ;
            options.addArguments("--user-agent=" + userAgent);
        }
        if (proxyIp != null) {
            message = message + "--proxy-server with proxy " + proxyIp+ "\n";
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

    /**
     * checks if an element es visible in the view port from the browser
     * @param element
     * @return
     */
    public static boolean isVisibleInViewport(WebElement element) {
        WebDriver driver = ((RemoteWebElement) element).getWrappedDriver();

        return (boolean) ((JavascriptExecutor) driver).executeScript(
                "var elem = arguments[0],                 " +
                "  box = elem.getBoundingClientRect(),    " +
                "  cx = box.left + box.width / 2,         " +
                "  cy = box.top + box.height / 2,         " +
                "  e = document.elementFromPoint(cx, cy); " +
                "for (; e; e = e.parentElement) {         " +
                "  if (e === elem)                        " +
                "    return true;                         " +
                "}                                        " +
                "return false;                            "
                , element);
    }

    public static Document transformwebDriverContentToJsoupDocument(WebDriver driver) {
        JavascriptExecutor js      = (JavascriptExecutor) driver;
        String             content = (String) js.executeScript("return document.documentElement.outerHTML;");
        Document           page    = Jsoup.parse(content);
        return page;
    }


    public static void waitUntilElementIsVisible(WebDriver webDriver, By visibleElement) {
        WebDriverWait wait = new WebDriverWait(webDriver, 100);
        wait.until(ExpectedConditions.visibilityOfElementLocated(visibleElement));
        sleepRandomTime(3000, 5000);
    }

    public static void waitUntilElementIsVisible(WebDriver webDriver, By visibleElement, int timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(webDriver, timeOutInSeconds);
        wait.until(ExpectedConditions.visibilityOfElementLocated(visibleElement));
        sleepRandomTime(3000, 5000);
    }

    public static void sleep(int timeInMillis) {
        try {
            Thread.sleep(timeInMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void  sleepRandomTime(int minInMillis, int maxInMillis) {
        int randomNum = ThreadLocalRandom.current().nextInt(minInMillis, maxInMillis + 1);
        try {
            Thread.sleep(randomNum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
