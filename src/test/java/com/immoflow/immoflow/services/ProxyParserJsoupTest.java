package com.immoflow.immoflow.services;

import com.immoflow.immoflow.resource.SimpleProxy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;

class ProxyParserJsoupTest {


    @Test
    void scrapeProxied() throws IOException, InterruptedException {
        ProxyParserJsoup proxyParserJsoup = new ProxyParserJsoup();
        proxyParserJsoup.scrapeProxies();
    }

    @Test
    void shouldUseProxyLimit() {
        ReflectionTestUtils.setField(ProxyParserJsoup.class, "workingProxyLimit", 3);
        ProxyParserJsoup  proxyParserJsoup = new ProxyParserJsoup();
        List<SimpleProxy> simpleProxies   = proxyParserJsoup.scrapeProxies();
        Assertions.assertEquals(3,simpleProxies.size());
    }

    @Test
    void shouldScrapeProxiesWithoutProxyProxyLimit() {
        ReflectionTestUtils.setField(ProxyParserJsoup.class, "workingProxyLimit", 999);
        ReflectionTestUtils.setField(ProxyParserJsoup.class, "timeOutForAllRunningThreadsInSec", 60);
        ReflectionTestUtils.setField(ProxyParserJsoup.class, "maxActiveThreadNumber", 20);
        ProxyParserJsoup  proxyParserJsoup = new ProxyParserJsoup();
        List<SimpleProxy> simpleProxies   = proxyParserJsoup.scrapeProxies();
        Assertions.assertTrue(simpleProxies.size()>1);
    }


}
