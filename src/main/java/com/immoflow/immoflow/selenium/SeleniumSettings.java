package com.immoflow.immoflow.selenium;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties("scraper.selenium")
@Data
public class SeleniumSettings implements InitializingBean {

    private static SeleniumSettings seleniumSettings;

    private String  driverpath;
    private boolean extensionsDisabled;
    private boolean incognitoMode;
    private boolean pluginDiscoveryDisabled;
    private boolean headless;
    private boolean startMaximized;

    @Override
    public void afterPropertiesSet() {
        seleniumSettings = this;
    }

    static SeleniumSettings getStaticSeleniumSettings() {
        return seleniumSettings;
    }

}

