package com.immoflow.immoflow.services;

import com.immoflow.immoflow.TestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertyParserManagerTest {

    @Test
    void getPropertyData() {
        PropertyParserManager propertyParserManager = new PropertyParserManager();
        TestUtils.setProxyProperties();
        propertyParserManager.getPropertyData();
    }

}
