package com.group6.accommodation.domain.reservation;

import static io.restassured.RestAssured.given;

import com.group6.accommodation.domain.reservation.model.dto.PostReserveRequestDto;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ReservationE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Container
    public static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:latest")
        .withDatabaseName("testdb")
        .withUsername("root")
        .withPassword("1234");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        initializeTestData();
    }


    private void initializeTestData() {
        // 회원 데이터 삽입
//        jdbcTemplate.execute("INSERT INTO users (id, username, password) VALUES (1, 'user', 'password')");
//        // 예약 데이터 삽입
//        jdbcTemplate.execute("INSERT INTO reservations (room_id, user_id, start_date, end_date, amount) VALUES (1, 1, '2024-07-02', '2024-07-03', 100)");
    }


    private String obtainAccessToken() {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", "test@example.com");
        loginRequest.put("password", "1234");

        Response response = given()
            .contentType("application/json")
            .body(loginRequest)
            .when()
            .post("/login")
            .then()
            .statusCode(200)
            .extract()
            .response();

        return response.jsonPath().getString("token");
    }





    @Test
    @DisplayName("에약 하기")
    public void reserve() {
        String token = obtainAccessToken();

        int headCount = 2;
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(2);
        int price = 5100;

        PostReserveRequestDto postReserveRequestDto = new PostReserveRequestDto();

    }



}
