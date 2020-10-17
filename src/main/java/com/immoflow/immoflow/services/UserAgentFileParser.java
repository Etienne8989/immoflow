package com.immoflow.immoflow.services;

import com.immoflow.immoflow.resource.UserAgentFromFile;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class UserAgentFileParser implements UserAgentParser<UserAgentFromFile> {

    private static final String USER_AGENT_FILE_URL = "src/main/resources/user_agents_chrome.csv";

    @Override
    public List<UserAgentFromFile> getUserAgentList() {

        File file = new File(USER_AGENT_FILE_URL);
        List<UserAgentFromFile> userAgentFromFileList = new ArrayList<>();
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                userAgentFromFileList.add(new UserAgentFromFile(myReader.nextLine()));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            log.debug(" user agent file not found", e);
        }

        return userAgentFromFileList;
    }


}
