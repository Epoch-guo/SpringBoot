package com.epoch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CompetitionApplication {
    private static final Logger log = LoggerFactory.getLogger(CompetitionApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(CompetitionApplication.class, args);
        log.info("CompetitionApplication started successfully.");
    }
}