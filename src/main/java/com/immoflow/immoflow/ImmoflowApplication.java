package com.immoflow.immoflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ImmoflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImmoflowApplication.class, args);
    }

}
