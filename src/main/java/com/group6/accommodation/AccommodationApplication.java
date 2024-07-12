package com.group6.accommodation;

import com.group6.accommodation.domain.accommodation.service.DatabaseInitializationService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class AccommodationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccommodationApplication.class, args);
	}

	@Bean
	public ApplicationRunner applicationRunner(DatabaseInitializationService databaseInitializationService) {
		return args -> databaseInitializationService.initializeDatabase();
	}

}
