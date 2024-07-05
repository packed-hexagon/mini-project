package com.group6.accommodation.domain.reservation.config;

import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

@Configuration
public class TestDatabaseConfig {

    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:latest")
        .withDatabaseName("test")
        .withUsername("test")
        .withPassword("test")
        .withExposedPorts(3306); // 포트 지정

    static {
        mySQLContainer.start();
    }

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
            .driverClassName("com.mysql.cj.jdbc.Driver")
            .url("jdbc:mysql://" + mySQLContainer.getHost() + ":" + mySQLContainer.getMappedPort(3306) + "/test")
            .username(mySQLContainer.getUsername())
            .password(mySQLContainer.getPassword())
            .build();
    }

    @AfterEach
    public void tearDown() {
        mySQLContainer.stop();
    }
}