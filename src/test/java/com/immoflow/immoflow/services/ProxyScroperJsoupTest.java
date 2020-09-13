package com.immoflow.immoflow.services;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ProxyScroperJsoupTest {


    @Test
    void scrapeProxied() throws IOException, InterruptedException {
        ProxyScroperJsoup proxyScroperJsoup = new ProxyScroperJsoup();
        proxyScroperJsoup.scrapeProxied();
    }

}
