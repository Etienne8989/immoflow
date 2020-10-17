package com.immoflow.immoflow.services;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class ProxyParserJsoupTest {


    @Test
    void scrapeProxied() throws IOException, InterruptedException {
        ProxyParserJsoup proxyParserJsoup = new ProxyParserJsoup();
        proxyParserJsoup.scrapeProxies();
    }

}
