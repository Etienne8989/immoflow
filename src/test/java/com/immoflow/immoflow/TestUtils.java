package com.immoflow.immoflow;

import com.immoflow.immoflow.resource.ProxyContext;
import com.immoflow.immoflow.proxies.ProxyParserJsoup;
import org.springframework.test.util.ReflectionTestUtils;

public class TestUtils {

    public static ProxyContext buildProxyContext(){
        ProxyContext proxyContext = new ProxyContext();
        proxyContext.setSslProxies(true);
        return proxyContext;
    }


    public static void setProxyProperties(){
        ReflectionTestUtils.setField(ProxyParserJsoup .class, "workingProxyLimit", 999);
        ReflectionTestUtils.setField(ProxyParserJsoup.class, "maxActiveThreadNumber", 50);
    }

}
