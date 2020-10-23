package com.immoflow.immoflow.proxies;

import java.util.List;

public interface ProxyParser<T> {

    List<T> scrapeProxies(ProxyContext proxyContext);

}
