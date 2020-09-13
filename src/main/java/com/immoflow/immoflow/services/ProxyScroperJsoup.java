package com.immoflow.immoflow.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class ProxyScroperJsoup {

    private static final String USER_AGENT        = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
    public static final String  URL_PROXIES_DAILY = "https://proxy-daily.com/";


    public void scrapeProxied() throws IOException, InterruptedException {
        //proxies scrapen
        //proxies von proxies-daily.com
        List<String> proxyList = scrapeProxiesFromProxiesDaily();

        //proxy-connection testen
        for(String ip : proxyList){
            String[] ipAndPort = ip.split(":");
            log.info("test connection with host " + ipAndPort[0] + " and port " + ipAndPort[1]);
            getIpByBufferReader(ipAndPort[0], ipAndPort[1]);
        }

        //wo connection erfolgreich war behalten und für weiteres scraping verwenden
    }

    private List<String> scrapeProxiesFromProxiesDaily() {
        log.info("connect to " + URL_PROXIES_DAILY);
        Document     page         = connectAndGetPage(URL_PROXIES_DAILY);
        String       proxies      = page.selectFirst(".freeProxyStyle:nth-child(7)").text();
        List<String> proxyList    = (List<String>) Arrays.asList(proxies.split("\\s"));
        proxyList.forEach(a-> a.replaceAll("\\s", ""));
        Collections.shuffle(proxyList);
        log.info("the following proxies has been scraped: ");
        log.info(proxyList.toString());
        return proxyList;
    }

    private void getIpWithJsoup(String proxyHost, String proxyPort){
        Document document = connectAndGetPage("https://www.showmyip.com", proxyHost, proxyPort);
        if (document != null) {
            Elements ip = document.select(".iptab td:nth-child(2)");
            log.info("the current ip is: " + ip.text());
        }

    }

    private void getIpWithGson() throws IOException {
        URL           url     = new URL("https://httpbin.org/ip");
        URLConnection request = url.openConnection();
        request.connect();
        JsonParser  jp      = new JsonParser();
        JsonElement root    = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        JsonObject rootobj = root.getAsJsonObject();
        String ip = rootobj.get("origin").getAsString();
        log.info("the current ip is " + ip);
    }

    private String getIpByBufferReader(String proxyHost, String proxyPort) throws InterruptedException, IOException {
        System.setProperty("https.proxySet", "true");
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", proxyPort);
        System.setProperty("https.proxyHost", proxyHost);
        System.setProperty("https.proxyPort", proxyPort);
        Thread.sleep(5000);

        URL            myIp = new URL("http://checkip.amazonaws.com");
        BufferedReader in   = new BufferedReader(new InputStreamReader(myIp.openStream()));
        String ip = in.readLine();
        log.info("the current ip is " + ip);
        return ip;
    }

    private Document connectAndGetPage(String url, String proxyHost, String proxyPort) {
        Document page = null;
        try {
            if (url.startsWith("http")) {

                //todo gucken wieso "java.net.ConnectException: Connection refused" bei connet kommt


                //connect to URL
                page = Jsoup.connect(
                        url)
                        .proxy(proxyHost, Integer.parseInt(proxyPort))
                        .timeout(10000)
                        .userAgent(USER_AGENT)
                        .get();
            } else {
                //connect to file
                File html = new File(url);
                page = Jsoup.parse(html, null);
            }

        } catch(SocketTimeoutException ex){
            log.info("the connection to " + proxyHost + ":" + proxyPort + " is not possible - (\"SocketTimeoutException\")");
        }
        catch (IOException ex) {
            log.debug("could not parse given website ", ex);
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
