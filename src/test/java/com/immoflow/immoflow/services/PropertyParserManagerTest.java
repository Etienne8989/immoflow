package com.immoflow.immoflow.services;

import com.immoflow.immoflow.TestUtils;
import com.immoflow.immoflow.selenium.SeleniumSettings;
import com.immoflow.immoflow.selenium.SeleniumUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest({SeleniumSettings.class})
@ActiveProfiles("test")
class PropertyParserManagerTest {

    @Test
    void getPropertyData() {
        PropertyParserManager propertyParserManager = new PropertyParserManager();
        TestUtils.setProxyProperties();
        propertyParserManager.getPropertyData();
    }

}
