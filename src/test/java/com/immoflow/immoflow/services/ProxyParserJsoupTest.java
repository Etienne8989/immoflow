package com.immoflow.immoflow.services;

import com.immoflow.immoflow.resource.ProxyContext;
import com.immoflow.immoflow.resource.SimpleProxy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.List;

class ProxyParserJsoupTest {


    @Test
    void scrapeProxied()  {
        ProxyParserJsoup proxyParserJsoup = new ProxyParserJsoup();
        proxyParserJsoup.scrapeProxies(buildProxyContext());
    }

    @Test
    void shouldUseProxyLimit() {
        ReflectionTestUtils.setField(ProxyParserJsoup.class, "workingProxyLimit", 3);
        ProxyParserJsoup  proxyParserJsoup = new ProxyParserJsoup();
        List<SimpleProxy> simpleProxies   = proxyParserJsoup.scrapeProxies(buildProxyContext());
        Assertions.assertEquals(3,simpleProxies.size());
    }

    @Test
    void shouldScrapeProxiesWithoutProxyProxyLimit() {
        ReflectionTestUtils.setField(ProxyParserJsoup.class, "workingProxyLimit", 999);
        ReflectionTestUtils.setField(ProxyParserJsoup.class, "maxActiveThreadNumber", 50);
        ProxyParserJsoup  proxyParserJsoup = new ProxyParserJsoup();
        List<SimpleProxy> simpleProxies   = proxyParserJsoup.scrapeProxies(buildProxyContext());
        Assertions.assertTrue(simpleProxies.size()>1);
    }

    ProxyContext buildProxyContext(){
        ProxyContext proxyContext = new ProxyContext();
        proxyContext.setSslProxies(true);
        return proxyContext;
    }


}
