package com.immoflow.immoflow.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.immoflow.immoflow.resource.SimpleProxy;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class ProxyParserJsoup implements ProxyParser<SimpleProxy> {

    //todo variablen als properties setzen
    private static final String  USER_AGENT_GOOGLE = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
    private static final String  USER_AGENT_REAL   = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36";
    private static final String  URL_PROXIES_DAILY = "https://proxy-daily.com/";
    private static final String  URL_SSL_PROXIES   = "https://www.sslproxies.org/";
    private static final String  URL_SHOW_MY_IP    = "https://www.showmyip.com";
    private static final Integer JSOUP_TIMEOUT     = 7000;


    public List<SimpleProxy> scrapeProxies() {
        ArrayList<String> proxyList        = screapeProxiesFromSllProxies();
        List<SimpleProxy>      workingProxyList = testProxyConnections(proxyList);
        log.info("\n\n the following proxies are working: \n\n" + workingProxyList);
        return workingProxyList;
    }

    private ArrayList<String> screapeProxiesFromSllProxies() {
        Document          page      = connectAndGetPage(URL_SSL_PROXIES);
        Element           tableBody = page.select("tbody").first();
        Elements          tableRows = tableBody.select("tr");
        ArrayList<String> proxyList = new ArrayList<>();
        for (Element e : tableRows) {
            String host        = e.select("td:nth-child(1)").text();
            String port        = e.select("td:nth-child(2)").text();
            String hostAndPort = host + ":" + port;
            proxyList.add(hostAndPort);
            Collections.shuffle(proxyList);
        }
        log.info("scraped proxies list: \n" + proxyList + "\n");
        return proxyList;
    }

    private List<SimpleProxy> testProxyConnections(ArrayList<String> proxyList) {
        log.info("the proxy testing is getting started...\n\n");
        List<SimpleProxy> workingProxiesList = new ArrayList<>();
        for (String ip : proxyList) {
            String[] ipAndPort = ip.split(":");
            log.info("test connection with host " + ipAndPort[0] + " and port " + ipAndPort[1]);
            SimpleProxy workingProxy = getIpWithJsoup(ipAndPort[0], ipAndPort[1]);
            if (workingProxy != null) {
                log.info("+++ the proxy is working and will be added to the proxy list +++\n");
                workingProxiesList.add(workingProxy);
            } else {
                log.info("the proxy is NOT working and and will be discarded\n");
            }
        }
        return workingProxiesList;
    }

    private SimpleProxy getIpWithJsoup(String proxyHost, String proxyPort) {
        Document document     = connectAndGetPage(URL_SHOW_MY_IP, proxyHost, proxyPort);
        SimpleProxy simpleProxy = null;
        if (document != null) {
            Elements ip = document.select(".iptab td:nth-child(2)");
            log.info("the current ip is: " + ip.text());
            simpleProxy = new SimpleProxy(proxyHost, proxyPort);
        }
        return simpleProxy;
    }

    private Document connectAndGetPage(String url, String proxyHost, String proxyPort) {
        Document page = null;
        try {
            if (url.startsWith("http")) {
                //connect to URL
                page = Jsoup.connect(
                        url)
                        .proxy(proxyHost, Integer.parseInt(proxyPort))
                        .timeout(JSOUP_TIMEOUT)
                        .userAgent(USER_AGENT_REAL)
                        .get();
            } else {
                //connect to file
                File html = new File(url);
                page = Jsoup.parse(html, null);
            }
        } catch (IOException ex) {
            log.info("the connection to " + proxyHost + ":" + proxyPort + " is not possible: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
        }
        return page;
    }


    Document connectAndGetPage(String url) {
        Document page = null;
        try {
            if (url.startsWith("http")) {
                //connect to URL
                page = Jsoup.connect(
                        url)
                        .userAgent(USER_AGENT_REAL)
                        .get();
            } else {
                //connect to file
                File html = new File(url);
                page = Jsoup.parse(html, null);
            }

        } catch (IOException ex) {
            log.debug("could not parse given website ", ex);
        }
        return page;
    }

    //proxies aren't working (try again later)
    private List<String> scrapeProxiesFromProxiesDaily() {
        log.info("connect to " + URL_PROXIES_DAILY);
        Document     page      = connectAndGetPage(URL_PROXIES_DAILY);
        String       proxies   = page.selectFirst(".freeProxyStyle:nth-child(7)").text();
        List<String> proxyList = (List<String>) Arrays.asList(proxies.split("\\s"));
        proxyList.forEach(a -> a.replaceAll("\\s", ""));
        Collections.shuffle(proxyList);
        log.info("the following proxies has been scraped: ");
        log.info(proxyList.toString());
        return proxyList;
    }

    //anternative to getIpWithJsoup
    private void getIpWithGson() throws IOException {
        URL           url     = new URL("https://httpbin.org/ip");
        URLConnection request = url.openConnection();
        request.connect();
        JsonParser  jp      = new JsonParser();
        JsonElement root    = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        JsonObject  rootobj = root.getAsJsonObject();
        String      ip      = rootobj.get("origin").getAsString();
        log.info("the current ip is " + ip);
    }

    //alternative to getIpWithJsoup
    private String getIpByBufferReader(String proxyHost, String proxyPort) throws InterruptedException, IOException {
        System.setProperty("https.proxySet", "true");
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", proxyPort);
        System.setProperty("https.proxyHost", proxyHost);
        System.setProperty("https.proxyPort", proxyPort);
        Thread.sleep(5000);

        URL            myIp = new URL("http://checkip.amazonaws.com");
        BufferedReader in   = new BufferedReader(new InputStreamReader(myIp.openStream()));
        String         ip   = in.readLine();
        log.info("the current ip is " + ip);
        return ip;
    }

}
