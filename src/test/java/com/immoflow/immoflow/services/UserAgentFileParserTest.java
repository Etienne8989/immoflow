package com.immoflow.immoflow.services;

import com.immoflow.immoflow.useragent.UserAgent;
import com.immoflow.immoflow.useragent.UserAgentFileParser;
import com.immoflow.immoflow.useragent.UserAgentParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserAgentFileParserTest {

    @Test
    void getUserAgentList() {
        UserAgentParser<UserAgent> userAgentParser = new UserAgentFileParser();
        List<UserAgent>            userAgentList   = userAgentParser.getUserAgentList();
        assertNotNull(userAgentList.get(0));
    }

}
