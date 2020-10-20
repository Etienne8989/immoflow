package com.immoflow.immoflow.services;

import com.immoflow.immoflow.TestUtils;
import com.immoflow.immoflow.resource.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PropertyParserSeleniumTest {

    // ggf auschecken für free proxies https://github.com/hyan15/scrapy-proxy-pool
    // seite um procxies zu kaufen https://www.hidemyass.com/de-de/index
    //video für proxies https://www.youtube.com/watch?v=090tLVr0l7s
    //video für user-agent https://www.youtube.com/watch?v=GOjuQ9IgSfI
    //Adresse für User-Agents -> https://pypi.org/project/scrapy-user-agents/

    private static final String BROWSER_INFO_URL = "https://www.whatismybrowser.com/detect/what-http-headers-is-my-browser-sending";
    private static final String IMMOSCOUT_PRODUCT_URL = "C:\\Users\\ehoven\\Documents\\Projekte\\immoflow\\src\\main\\resources\\scrapedata\\immflow-scrape-page\\testpage.html";
    private static final String IMMOSCOUT_ARCHIVE_URL = "https://www.immobilienscout24.de/Suche/de/nordrhein-westfalen/duesseldorf/wohnung-mieten?enteredFrom=one_step_search";

    public static final String REAL_USER_AGENT        = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36";
    public static final String GOOGLE_BOT_USER_AGENT1 = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
    public static final String GOOGLE_BOT_USER_AGENT2 = "Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko; compatible; Googlebot/2.1; +http://www.google.com/bot.html) Chrome/W.X.Y.Z‡ Safari/537.36";
    public static final String GOOGLE_BOT_USER_AGENT3 = "Googlebot/2.1 (+http://www.google.com/bot.html)";


    public static final String URL = "C:\\Users\\ehoven\\Documents\\Projekte\\immoflow\\src\\main\\resources\\scrapedata\\immflow-scrape-page\\testpage.html";

    @Test
    @Disabled
    void scrapeData() throws IOException {

//        ProxyParserJsoup proxyParserJsoup = new ProxyParserJsoup();
//        TestUtils.setProxyProperties();
//        List<SimpleProxy>   workingProxies      = proxyParserJsoup.scrapeProxies(TestUtils.buildProxyContext());
//        Collections.shuffle(workingProxies);
//        UserAgentFileParser userAgentFileParser = new UserAgentFileParser();
//        List<UserAgent>        userAgentList          = userAgentFileParser.getUserAgentList();
//        Collections.shuffle(userAgentList);
//        String proxy = workingProxies.get(0).getHost() + ":" + workingProxies.get(0).getPort();
//                PropertyParserSelenium propertyParserSelenium = new PropertyParserSelenium();
//        WebDriver          webDriver = SeleniumUtils.initWebDriver(proxy, userAgentList.get(0).getUserAgent());
//        JavascriptExecutor js        = (JavascriptExecutor)webDriver;
//        webDriver.get("https://www.immobilienscout24.de/expose/122659785?referrer=RESULT_LIST_LISTING&navigationServiceUrl=%2FSuche%2Fcontroller%2FexposeNavigation%2Fnavigate.go%3FsearchUrl%3D%2FSuche%2Fde%2Fnordrhein-westfalen%2Fduesseldorf%2Fwohnung-mieten%3FenteredFrom%253Done_step_search%26exposeId%3D122659785&navigationHasPrev=true&navigationHasNext=true&navigationBarType=RESULT_LIST&searchId=6d92fda4-44c2-38ad-8335-56327fb5862d&searchType=district#/");
//        String body = (String) js.executeScript("return document.documentElement.outerHTML;");
//        System.out.println(body);


        String content = Files.readString(Path.of("/Users/etho/projects-programming/immoflow/src/main/resources/scrapedata/immflow-scrape-page/test-page-html-string.txt"), StandardCharsets.UTF_8);
        Document page = Jsoup.parse(content);
        PropertyParserJsoup propertyParserJsoup = new PropertyParserJsoup();
        PropertyData propertyData = propertyParserJsoup.scrapeData(page);
        Assertions.assertEquals(propertyData.getCity(), "Düsseldorf");

    }

}
