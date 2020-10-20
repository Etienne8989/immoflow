package com.immoflow.immoflow.services;

import com.immoflow.immoflow.resource.ProxyContext;
import com.immoflow.immoflow.resource.SimpleProxy;
import com.immoflow.immoflow.useragent.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class ScrapeUtilsJsoup {


    private Document getPageByContext(String basicUrl, List<UserAgent> userAgentList, List<SimpleProxy> workingProxies) {
        Document page = null;
        if (!workingProxies.isEmpty() && !userAgentList.isEmpty()) {
            page = connectAndGetPage(basicUrl, workingProxies.get(0), userAgentList.get(0));
        } else if (!userAgentList.isEmpty()) {
            page = connectAndGetPage(basicUrl, userAgentList.get(0));
        } else {
            page = connectAndGetPage(basicUrl);
        }
        return page;
    }


    Document connectAndGetPage(String basicUrl, SimpleProxy proxy, UserAgent userAgent) {
        Document page = null;
        try {
            if (basicUrl.startsWith("http")) {
                log.info("the immo scraper will connect with proxy {}:{} and fake user agent {}", proxy.getHost(), proxy.getPort(), userAgent.getUserAgent());
                page = Jsoup.connect(basicUrl)
                        .proxy(proxy.getHost(), Integer.parseInt(proxy.getPort()))
                        .userAgent(userAgent.getUserAgent())
                        .get();

            } else {
                //connect to file
                File html = new File(basicUrl);
                page = Jsoup.parse(html, null);
            }

        } catch (IOException ex) {
            log.debug("could not parse given website ", ex);
        }
        return page;
    }


    Document connectAndGetPage(String basicUrl, UserAgent userAgent) {
        Document page = null;
        try {
            if (basicUrl.startsWith("http")) {

                log.info("No proxy available. the immo scraper will connect without proxy but with fake user agent {}", userAgent.getUserAgent());
                page = Jsoup.connect(basicUrl)
                        .userAgent(userAgent.getUserAgent())
                        .get();

            } else {
                //connect to file
                File html = new File(basicUrl);
                page = Jsoup.parse(html, null);
            }

        } catch (IOException ex) {
            log.debug("could not parse given website ", ex);
        }
        return page;
    }

    Document connectAndGetPage(String basicUrl) {
        Document page = null;
        try {
            if (basicUrl.startsWith("http")) {

                log.info("No Proxy or fake agent available. The immo scraper will connect with your actual ip");
                page = Jsoup.connect(basicUrl)
                        .get();

            } else {
                //connect to file
                File html = new File(basicUrl);
                page = Jsoup.parse(html, null);
            }

        } catch (IOException ex) {
            log.debug("could not parse given website ", ex);
        }
        return page;
    }

    public static ProxyContext buildProxyContext(){
        ProxyContext proxyContext = new ProxyContext();
        proxyContext.setSslProxies(true);
        return proxyContext;
    }


}
