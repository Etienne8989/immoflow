package com.immoflow.immoflow.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.immoflow.immoflow.resource.PropertyData;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ProxyScroperJsoup {

    private static final String USER_AGENT        = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
    public static final String  URL_PROXIES_DAILY = "https://proxy-daily.com/";


    public void scrapeProxied() throws IOException {
        //proxies scrapen
        PropertyData propertyData = new PropertyData();

        //proxies von proxies-daily.com
        log.info("connect to " + URL_PROXIES_DAILY);
        Document     page         = connectAndGetPage(URL_PROXIES_DAILY);
        String       proxies      = page.selectFirst(".freeProxyStyle:nth-child(7)").text();
        List<String> proxyList    = (List<String>) Arrays.asList(proxies.split("\\s"));
        proxyList.forEach(a-> a.replaceAll("\\s", ""));
        log.info("the following proxies has been scraped: ");
        log.info(proxyList.toString());

        //proxy-connection testen
        for(String ip : proxyList){
            String[] ipAndPort = ip.split(":");
            log.info("test connection with host: " + ipAndPort[0] + " and port " + ipAndPort[1]);
            //todo gucken wieso connection mit gesetzter ip nicht funkzioniert (es wird immer die wirkliche ip angezeigt)
            System.setProperty("http.proxyHost", ipAndPort[0]);
            System.setProperty("http.proxyPort", ipAndPort[1]);
            getIp();
        }

        //wo connection erfolgreich war behalten und für weiteres scraping verwenden


    }

    private void getIp() throws IOException {
        URL           url     = new URL("https://httpbin.org/ip");
        URLConnection request = url.openConnection();
        request.connect();
        JsonParser  jp      = new JsonParser();
        JsonElement root    = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        JsonObject rootobj = root.getAsJsonObject();
        String ip = rootobj.get("origin").getAsString();
        log.info("the current ip is " + ip);
    }

    Document connectAndGetPage(String url) {
        Document page = null;
        try {
            if (url.startsWith("http")) {
                //connect to URL
                page = Jsoup.connect(
                        url)
                        .userAgent(USER_AGENT)
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

}
