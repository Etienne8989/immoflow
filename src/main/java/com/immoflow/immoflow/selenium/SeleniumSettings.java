package com.immoflow.immoflow.selenium;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

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

    static SeleniumSettings getStaticSeleniumSettings(){
        return seleniumSettings;
    }

}

