package com.group6.accommodation.domain.accommodation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.accommodation.domain.accommodation.config.OpenapiConfig;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.likes.repository.UserLikeRepository;
import com.group6.accommodation.global.exception.error.AccommodationErrorCode;
import com.group6.accommodation.global.exception.type.AccommodationException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
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
@Profile("openapi")
public class AccommodationApiService {
    private final UserLikeRepository userLikeRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final OpenapiConfig openapiConfig;


    private static final int NUM_OF_ROWS = 10;

    @Cacheable("accommodations")
    public List<AccommodationEntity> fetchAllAccommodations() {
        try {
            int totalCount = getTotalCount();
            int totalPages = (int) Math.ceil((double) totalCount / NUM_OF_ROWS);
            int testPages = 10;

            List<CompletableFuture<List<AccommodationEntity>>> futures = new ArrayList<>();
            for (int pageNo = 1; pageNo <= testPages; pageNo++) {
                futures.add(fetchAccommodationsAsync(pageNo));
            }

            return futures.stream()
                    .map(CompletableFuture::join)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new AccommodationException(AccommodationErrorCode.NOT_FOUND_DATA_PAGE);
        }
    }

    private int getTotalCount() throws URISyntaxException, JsonProcessingException {
        try {
            URI uri = new URI(openapiConfig.getBaseUrl() + "?serviceKey=" + openapiConfig.getApiKey() + "&numOfRows=1&pageNo=1&MobileOS=AND&MobileApp=TestApp&_type=json");
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            HttpStatusCode statusCode = responseEntity.getStatusCode();

            if (statusCode == HttpStatus.OK) {
                JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());
                // System.out.println("rootNode : " + rootNode);
                return rootNode.path("response").path("body").path("totalCount").asInt();
            }
        } catch (URISyntaxException e) {
            throw new AccommodationException(AccommodationErrorCode.ERROR_URI); // URI가 잘못된 경우
        } catch (RestClientException e) {
            throw new AccommodationException(AccommodationErrorCode.ERROR_RESTEMPLATE); // RestTemplate 호출에 문제가 있는 경우
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
                JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());
                JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");
                List<AccommodationEntity> accommodations = new ArrayList<>();

                for (JsonNode itemNode : itemsNode) {
                    AccommodationEntity accommodation = parseAccommodation(itemNode);
                    accommodations.add(accommodation);
                }

                return CompletableFuture.completedFuture(accommodations);
            }

            return CompletableFuture.completedFuture(Collections.emptyList());
        } catch (Exception e) {
            throw new AccommodationException(AccommodationErrorCode.NOT_FOUND_DATA_PAGE);
        }
    }

    private AccommodationEntity parseAccommodation(JsonNode itemNode) {
        AccommodationEntity accommodation = new AccommodationEntity();
        accommodation.setId(itemNode.path("contentid").asLong());
        accommodation.setTitle(truncate(removeTextWithinBrackets(itemNode.path("title").asText()), 20));
        accommodation.setAddress(itemNode.path("addr1").asText());
        accommodation.setAddress2(itemNode.path("addr2").asText(""));
        accommodation.setAreacode(itemNode.path("areacode").asText());
        accommodation.setSigungucode(itemNode.path("sigungucode").asInt());
        accommodation.setCategory(itemNode.path("cat3").asText());

        String firstImage = itemNode.path("firstimage").asText();
        accommodation.setImage(firstImage.isEmpty() ? "http://tong.visitkorea.or.kr/cms/resource/02/2493702_image2_1.jpg" : firstImage);

        String thumbnail = itemNode.path("firstimage2").asText();
        accommodation.setThumbnail(thumbnail.isEmpty() ? "http://tong.visitkorea.or.kr/cms/resource/02/2493702_image2_1.jpg" : thumbnail);

        accommodation.setLatitude(itemNode.path("mapy").asDouble());
        accommodation.setLongitude(itemNode.path("mapx").asDouble());
        accommodation.setMlevel(itemNode.path("mlevel").asInt(0));

        String tel = itemNode.path("tel").asText();
        accommodation.setTel(tel.isEmpty() ? "010-1234-5678" : truncate(replaceTextWithinAngleBrackets(tel), 255));

        int likeCount = userLikeRepository.countByAccommodationId(itemNode.path("contentid").asLong());
        accommodation.setLikeCount(likeCount);

        double[] ratings = {2.5, 3.0, 3.5, 4.0, 4.5, 5.0};
        Random random = new Random();
        accommodation.setRating(ratings[random.nextInt(ratings.length)]);

        return accommodation;
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
