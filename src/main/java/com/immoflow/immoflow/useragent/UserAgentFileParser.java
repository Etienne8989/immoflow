package com.immoflow.immoflow.useragent;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class UserAgentFileParser implements UserAgentParser<UserAgent> {

    private static final String USER_AGENT_FILE_URL = "src/main/resources/user_agents_chrome.csv";

    @Override
    public List<UserAgent> getUserAgentList() {

        File            file          = new File(USER_AGENT_FILE_URL);
        List<UserAgent> userAgentList = new ArrayList<>();
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                userAgentList.add(new UserAgent(myReader.nextLine()));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            log.debug(" user agent file not found", e);
        }

        return userAgentList;
    }


}
