package com.immoflow.immoflow.services;

import com.immoflow.immoflow.resource.UserAgentFromFile;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserAgentFileParserTest {

    @Test
    void getUserAgentList() {
        UserAgentParser<UserAgentFromFile> userAgentParser = new UserAgentFileParser();
        List<UserAgentFromFile> userAgentList = userAgentParser.getUserAgentList();
        assertNotNull(userAgentList.get(0));
    }

}
