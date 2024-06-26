package com.group6.accommodation.domain.accommodation.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.accommodation.domain.accommodation.config.OpenapiConfig;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationDetailResponseDto;
import com.group6.accommodation.domain.accommodation.model.dto.AccommodationResponseDto;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.model.enums.Area;
import com.group6.accommodation.domain.accommodation.model.enums.Category;
import com.group6.accommodation.global.exception.error.AccommodationErrorCode;
import com.group6.accommodation.global.exception.type.AccommodationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
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
        int tempPrice = 150000;
        try {
            URI uri = new URI(openapiConfig.getBaseUrlRoom() + "?serviceKey=" + openapiConfig.getApiKey() + "&contentTypeId=32&contentId=" + accommodationId + "&MobileOS=ETC&MobileApp=TestApp&_type=json");
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String responseBody = responseEntity.getBody();
                JsonNode rootNode = objectMapper.readTree(responseBody);
                JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

                // 객실 정보 자체가 존재하는지
                if (itemsNode.isArray()) {
                    int minPrice = Integer.MAX_VALUE;

                    for (JsonNode itemNode : itemsNode) {
                        int roomPrice = itemNode.path("roomoffseasonminfee1").asInt();

                        if (roomPrice < minPrice) {
                            minPrice = roomPrice;
                        }
                    }

                    // 가격 정보가 0 으로만 존재한다면
                    if (minPrice == 0) {
                        return tempPrice;
                    } else {
                        return minPrice;
                    }
                }
                // 객실 정보가 존재하지 않으면 임시 가격
                else {
                    return tempPrice;
                }
            }
        } catch (URISyntaxException e) {
            throw new AccommodationException(AccommodationErrorCode.ERROR_URI); // URI가 잘못된 경우
        } catch (RestClientException e) {
            throw new AccommodationException(AccommodationErrorCode.ERROR_RESTEMPLATE); // RestTemplate 호출에 문제가 있는 경우
        } catch (JsonProcessingException e) {
            throw new AccommodationException(AccommodationErrorCode.ERROR_JSON_PARSING); // Json에 문제가 생겼을 경우
        }

        return tempPrice;
    }
}
