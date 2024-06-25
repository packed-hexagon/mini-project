package com.group6.accommodation.domain.accommodation.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.accommodation.domain.accommodation.config.OpenapiConfig;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDetailResponseDto;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationResponseDto;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.model.enums.Area;
import com.group6.accommodation.domain.accommodation.model.enums.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccommodationConverter {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final OpenapiConfig openapiConfig;

    public AccommodationResponseDto toDto(AccommodationEntity accommodation) {
        int minPrice = getMinRoomPrice(accommodation.getId());

        return AccommodationResponseDto.builder()
                .id(accommodation.getId())
                .title(accommodation.getTitle())
                .address(accommodation.getAddress())
                .address2(accommodation.getAddress2())
                .category(Category.getNameByCode(accommodation.getCategory()))
                .image(accommodation.getImage())
                .thumbnail(accommodation.getThumbnail())
                .price(minPrice)
                .latitude(accommodation.getLatitude())
                .longitude(accommodation.getLongitude())
                .likeCount(accommodation.getLikeCount())
                .rating(accommodation.getRating())
                .build();
    }

    public List<AccommodationResponseDto> toDtoList(List<AccommodationEntity> accommodationEntityList) {
        return accommodationEntityList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public AccommodationDetailResponseDto toDetailDto(AccommodationEntity accommodation) {
        int minPrice = getMinRoomPrice(accommodation.getId());

        return AccommodationDetailResponseDto.builder()
                .id(accommodation.getId())
                .title(accommodation.getTitle())
                .address(accommodation.getAddress())
                .address2(accommodation.getAddress2())
                .areacode(Area.getNameByCode(accommodation.getAreacode()))
                .sigungucode(accommodation.getSigungucode())
                .category(Category.getNameByCode(accommodation.getCategory()))
                .image(accommodation.getImage())
                .thumbnail(accommodation.getThumbnail())
                .price(minPrice)
                .latitude(accommodation.getLatitude())
                .longitude(accommodation.getLongitude())
                .mlevel(accommodation.getMlevel())
                .tel(accommodation.getTel())
                .likeCount(accommodation.getLikeCount())
                .rating(accommodation.getRating())
                .build();
    }

    private int getMinRoomPrice(Long accommodationId) {
        try {
            URI uri = new URI(openapiConfig.getBaseUrlRoom() + "?serviceKey=" + openapiConfig.getApiKey() + "&contentTypeId=32&contentId=" + accommodationId + "&MobileOS=ETC&MobileApp=TestApp&_type=json");
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            int tempPrice = 150000;

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                // JSON 응답을 로그에 출력
                String responseBody = responseEntity.getBody();
                System.out.println("API Response: " + responseBody);

                JsonNode rootNode = objectMapper.readTree(responseBody);
                JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

                // itemsNode가 배열인지 확인
                if (itemsNode.isArray()) {
                    int minPrice = Integer.MAX_VALUE;

                    for (JsonNode itemNode : itemsNode) {
                        int roomPrice = itemNode.path("roomoffseasonminfee1").asInt();
                        System.out.println("Room Price: " + roomPrice); // 각 객실의 가격을 로그에 출력
                        if (roomPrice < minPrice) {
                            minPrice = roomPrice;
                        }
                    }

                    if (minPrice == 0) {
                        return tempPrice;
                    } else {
                        return minPrice;
                    }
                } else if(itemsNode.equals("")) {
                    return tempPrice;
                } else {
                    System.out.println("Items node is not an array");
                }
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }


}
