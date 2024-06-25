package com.group6.accommodation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("dev")
public class AccommodationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccommodationApplication.class, args);
	}

}
