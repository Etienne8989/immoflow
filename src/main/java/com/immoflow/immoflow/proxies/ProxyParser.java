package com.immoflow.immoflow.proxies;

import com.immoflow.immoflow.resource.ProxyContext;

import java.util.List;

public interface ProxyParser<T> {

    List<T> scrapeProxies(ProxyContext proxyContext);

}
