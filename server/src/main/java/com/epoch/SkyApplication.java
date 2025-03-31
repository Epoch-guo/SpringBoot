package com.epoch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SkyApplication{
    private static final Logger log = LoggerFactory.getLogger(SkyApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(SkyApplication.class, args);
        log.info("SkyApplication started successfully.");
    }
}