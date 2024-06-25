package com.group6.accommodation;

import com.group6.accommodation.domain.accommodation.service.ApiProcessService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class AccommodationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccommodationApplication.class, args);
	}

	@Bean
	public ApplicationRunner applicationRunner(ApiProcessService apiProcessService) {
		return args -> {
			try {
				apiProcessService.processAccommodations();
			} catch (Exception e) {
				// 예외 처리 로직
				e.printStackTrace();
			}
		};
	}

}
