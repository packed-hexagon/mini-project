package com.group6.accommodation.domain.accommodation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.accommodation.domain.accommodation.config.OpenapiConfig;
import com.group6.accommodation.domain.accommodation.model.dto.ApiResponseDto;
import com.group6.accommodation.domain.accommodation.model.dto.ApiResponseDto.Item;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.likes.repository.UserLikeRepository;
import com.group6.accommodation.global.exception.error.AccommodationErrorCode;
import com.group6.accommodation.global.exception.type.AccommodationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccommodationApiService {
    private final UserLikeRepository userLikeRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final OpenapiConfig openapiConfig;

    private static final int NUM_OF_ROWS = 10;

    public List<AccommodationEntity> fetchAllAccommodations() {
        try {
            int totalCount = getTotalCount();
            int totalPages = (int) Math.ceil((double) totalCount / NUM_OF_ROWS);
            // int testPages = 10;

            List<CompletableFuture<List<AccommodationEntity>>> futures = new ArrayList<>();
            for (int pageNo = 1; pageNo <= 10; pageNo++) {
                futures.add(fetchAccommodationsAsync(pageNo));
            }

            return futures.stream()
                    .map(CompletableFuture::join)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new AccommodationException(AccommodationErrorCode.API_RESPONSE_ERROR);
        }
    }

    // 현재 데이터의 총 개수를 받아오는 메서드
    private int getTotalCount() {
        try {
            URI uri = new URI(openapiConfig.getBaseUrl() + "?serviceKey=" + openapiConfig.getApiKey() + "&numOfRows=1&pageNo=1&MobileOS=AND&MobileApp=TestApp&_type=json");
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            HttpStatusCode statusCode = responseEntity.getStatusCode();

            if (statusCode == HttpStatus.OK) {
                ApiResponseDto responseDto = objectMapper.readValue(responseEntity.getBody(), ApiResponseDto.class);
                return responseDto.getResponse().getBody().getTotalCount();
            }
        } catch (URISyntaxException e) {
            throw new AccommodationException(AccommodationErrorCode.ERROR_URI); // URI가 잘못된 경우
        } catch (RestClientException e) {
            throw new AccommodationException(AccommodationErrorCode.ERROR_RESTEMPLATE); // RestTemplate 호출에 문제가 있는 경우
        } catch (JsonProcessingException e) {
            throw new AccommodationException(AccommodationErrorCode.ERROR_JSON_PARSING); // Json에 문제가 생겼을 경우
        }
        return 0;
    }

    @Async
    @Retryable(value = RestClientException.class, maxAttempts = 3)
    public CompletableFuture<List<AccommodationEntity>> fetchAccommodationsAsync(int pageNo) {
        try {
            URI uri = new URI(openapiConfig.getBaseUrl() + "?serviceKey=" + openapiConfig.getApiKey() + "&numOfRows=" + NUM_OF_ROWS + "&pageNo=" + pageNo + "&MobileOS=AND&MobileApp=TestApp&_type=json");
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                ApiResponseDto responseDto = objectMapper.readValue(responseEntity.getBody(), ApiResponseDto.class);
                List<Item> items = responseDto.getResponse().getBody().getItems().getItem();
                List<AccommodationEntity> accommodations = items.stream()
                        .map(this::parseAccommodation)
                        .collect(Collectors.toList());

                return CompletableFuture.completedFuture(accommodations);
            }

            return CompletableFuture.completedFuture(Collections.emptyList());
        } catch (Exception e) {
            throw new AccommodationException(AccommodationErrorCode.API_RESPONSE_ERROR);
        }
    }

    private AccommodationEntity parseAccommodation(Item item) {
        Long id = item.getContentid();
        String title = truncate(removeTextWithinBrackets(item.getTitle()), 20);
        String address = item.getAddr1() != null ? item.getAddr1() : "";
        String address2 = item.getAddr2() != null ? item.getAddr2() : "";
        String areacode = item.getAreacode() != null ? item.getAreacode() : "";
        int sigungucode = 0;
        try {
            sigungucode = item.getSigungucode() != null ? Integer.parseInt(item.getSigungucode()) : 0;
        } catch (NumberFormatException e) {

        }
        String category = item.getCat3() != null ? item.getCat3() : "";

        String defaultImage = "http://tong.visitkorea.or.kr/cms/resource/02/2493702_image2_1.jpg";
        String image = (item.getFirstimage() != null && !item.getFirstimage().isEmpty()) ? item.getFirstimage() : defaultImage;
        String thumbnailImage = (item.getFirstimage2() != null && !item.getFirstimage2().isEmpty()) ? item.getFirstimage2() : defaultImage;

        Double latitude = item.getMapy() != null ? item.getMapy() : 0.0;
        Double longitude = item.getMapx() != null ? item.getMapx() : 0.0;
        Integer mlevel = item.getMlevel() != null ? item.getMlevel() : 0;

        String telNumber = (item.getTel() != null && !item.getTel().isEmpty()) ?
                truncate(replaceTextWithinAngleBrackets(item.getTel()), 255) : "010-1234-5678";

        int likeCount = userLikeRepository.countByAccommodationId(id);

        double[] ratings = {2.5, 3.0, 3.5, 4.0, 4.5, 5.0};
        Random random = new Random();
        double rating = ratings[random.nextInt(ratings.length)];

        return new AccommodationEntity(id, title, address, address2, areacode, sigungucode, category, image, thumbnailImage,
                latitude, longitude, mlevel, telNumber, likeCount, rating);
    }

    private String removeTextWithinBrackets(String text) {
        return text.replaceAll("\\[.*?\\]", "").trim();
    }

    private String replaceTextWithinAngleBrackets(String text) {
        return text.replaceAll("<.*?>", ",").trim();
    }

    private String truncate(String value, int length) {
        if (value.length() <= length) {
            return value;
        }
        return value.substring(0, length);
    }
}
