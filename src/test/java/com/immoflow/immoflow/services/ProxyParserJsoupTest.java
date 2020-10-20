package com.immoflow.immoflow.services;

import com.immoflow.immoflow.TestUtils;
import com.immoflow.immoflow.proxies.ProxyParserJsoup;
import com.immoflow.immoflow.resource.SimpleProxy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

class ProxyParserJsoupTest {


    @Test
    void scrapeProxied()  {
        ProxyParserJsoup proxyParserJsoup = new ProxyParserJsoup();
        proxyParserJsoup.scrapeProxies(TestUtils.buildProxyContext());
    }

    @Test
    void shouldUseProxyLimit() {
        ReflectionTestUtils.setField(ProxyParserJsoup.class, "workingProxyLimit", 3);
        ProxyParserJsoup  proxyParserJsoup = new ProxyParserJsoup();
        List<SimpleProxy> simpleProxies    = proxyParserJsoup.scrapeProxies(TestUtils.buildProxyContext());
        Assertions.assertEquals(3,simpleProxies.size());
    }

    @Test
    void shouldScrapeProxiesWithoutProxyProxyLimit() {
        ReflectionTestUtils.setField(ProxyParserJsoup.class, "workingProxyLimit", 999);
        ReflectionTestUtils.setField(ProxyParserJsoup.class, "maxActiveThreadNumber", 50);
        ProxyParserJsoup  proxyParserJsoup = new ProxyParserJsoup();
        List<SimpleProxy> simpleProxies   = proxyParserJsoup.scrapeProxies(TestUtils.buildProxyContext());
        Assertions.assertTrue(simpleProxies.size()>1);
    }




}
