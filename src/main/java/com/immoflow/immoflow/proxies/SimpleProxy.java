package com.immoflow.immoflow.proxies;

public class SimpleProxy {

    private String host;
    private String port;

    public SimpleProxy(String host, String port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

}
