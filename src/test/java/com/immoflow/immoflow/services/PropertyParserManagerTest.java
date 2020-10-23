package com.immoflow.immoflow.services;

import com.immoflow.immoflow.TestUtils;
import com.immoflow.immoflow.proxies.ProxyParserJsoup;
import com.immoflow.immoflow.proxies.SimpleProxy;
import com.immoflow.immoflow.selenium.SeleniumSettings;
import com.immoflow.immoflow.useragent.UserAgent;
import com.immoflow.immoflow.useragent.UserAgentFileParser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

@WebMvcTest({SeleniumSettings.class, ProxyParserJsoup.class})
@ActiveProfiles("test")
class PropertyParserManagerTest {

    //ElementClickInterceptedException

    @Test
    void getPropertyData() {

        //get proxies
        ProxyParserJsoup  proxyParserJsoup = new ProxyParserJsoup();
        List<SimpleProxy> workingProxies   = proxyParserJsoup.scrapeProxies(ScrapeUtilsJsoup.buildProxyContext());
        Collections.shuffle(workingProxies);
        //get useragents
        UserAgentFileParser userAgentFileParser = new UserAgentFileParser();
        List<UserAgent>     userAgentList       = userAgentFileParser.getUserAgentList();
        Collections.shuffle(userAgentList);
        //get property data with selenium
        PropertyParserManager propertyParserManager = new PropertyParserManager();
        TestUtils.setProxyProperties();
        propertyParserManager.getPropertyData(workingProxies, userAgentList);

    }

}
