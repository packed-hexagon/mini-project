package com.group6.accommodation;

import com.group6.accommodation.domain.accommodation.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OpenapiApplication implements ApplicationRunner {

    @Autowired
    private MainService mainService;

    public static void main(String[] args) {
        SpringApplication.run(OpenapiApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            mainService.processAccommodations();
        } catch (Exception e) {
            e.printStackTrace(); // 예외 처리
        }
    }
}