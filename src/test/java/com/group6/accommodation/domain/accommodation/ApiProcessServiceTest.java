package com.group6.accommodation.domain.accommodation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.domain.accommodation.service.AccommodationApiService;
import com.group6.accommodation.domain.accommodation.service.ApiProcessService;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ApiProcessServiceTest {

    @Autowired
    private ApiProcessService apiProcessService;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @MockBean
    private AccommodationApiService accommodationApiService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        accommodationRepository.deleteAll();
    }

    @Test
    public void testProcessAccommodations() throws IOException {
        // Given : Mock Api response
        JsonNode mockApiResponse = objectMapper.readTree(new ClassPathResource("mock_openapi_response.json").getFile());
        List<AccommodationEntity> mockAccommodations = List.of(
                new AccommodationEntity(1L, "Test Title 1", "Test Address 1", "Test Address2", "001", 123, "HOTEL", "image1.jpg", "thumbnail1.jpg", 37.5665, 126.9780, 0, "010-1234-5678", 0, 4.5),
                new AccommodationEntity(2L, "Test Title 2", "Test Address 2", "Test Address2", "002", 123, "HOTEL", "image2.jpg", "thumbnail2.jpg", 37.5665, 126.9780, 0, "010-1234-5678", 0, 4.5)
        );
        when(accommodationApiService.fetchAllAccommodations()).thenReturn(mockAccommodations);

        // When : Process accommodations
        apiProcessService.processAccommodations();

        // Then
        List<AccommodationEntity> savedAccommodations = accommodationRepository.findAll();
        Assertions.assertThat(savedAccommodations).hasSize(2);
        Assertions.assertThat(savedAccommodations).extracting(AccommodationEntity::getTitle).contains("Test Title 1", "Test Title 2");
    }

//    @Test
//    public void testIsDatabaseEmpty() {
//        // Given
//
//        // When
//        boolean isEmpty = apiProcessService.isDatabaseEmpty();
//
//        // Then
//        Assertions.assertThat(isEmpty).isTrue();
//
//        accommodationRepository.save(new AccommodationEntity(1L, "Test Title 1", "Test Address 1", "Test Address2", "001", 123, "HOTEL", "image1.jpg", "thumbnail1.jpg", 37.5665, 126.9780, 0, "010-1234-5678", 0, 4.5));
//
//        isEmpty = apiProcessService.isDatabaseEmpty();
//        Assertions.assertThat(isEmpty).isFalse();
//    }
}
