package com.group6.accommodation.domain.room.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.accommodation.domain.accommodation.config.OpenapiConfig;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.domain.room.model.entity.RoomEntity;
import com.group6.accommodation.global.exception.error.RoomErrorCode;
import com.group6.accommodation.global.exception.type.RoomException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomApiService {

	private final AccommodationRepository accommodationRepository;
	private final ObjectMapper objectMapper;
	private final RestTemplate restTemplate;
	private final OpenapiConfig openapiConfig;

	private static final int CONTENT_TYPE_ID = 32;

	@Cacheable("rooms")
	public List<RoomEntity> fetchAllRooms() {
		try {
			List<AccommodationEntity> accommodationEntities = accommodationRepository.findAll().subList(0, 100);
			List<CompletableFuture<List<RoomEntity>>> futures = new ArrayList<>();
			for (AccommodationEntity accommodation : accommodationEntities) {
				int contentId = Math.toIntExact(accommodation.getId());
				futures.add(fetchRoomsAsync(contentId));
			}
			return futures.stream()
				.map(CompletableFuture::join)
				.flatMap(List::stream)
				.collect(Collectors.toList());
		} catch (Exception e) {
			throw new RoomException(RoomErrorCode.NOT_FOUND_DATA_PAGE);
		}
	}

	@Async
	@Retryable(value = RestClientException.class, maxAttempts = 3)
	public CompletableFuture<List<RoomEntity>> fetchRoomsAsync(int contentId) {
		try {
			URI uri = new URI(openapiConfig.getBaseUrlRoom() + "?serviceKey=" + openapiConfig.getApiKey()
				+ "&contentTypeId=" + CONTENT_TYPE_ID + "&contentId=" + contentId + "&MobileOS=AND&MobileApp=TestApp&_type=json");
			ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());
				JsonNode itemsNode = rootNode.path("response").path("body").path("items")
					.path("item");
				List<RoomEntity> rooms = new ArrayList<>();

				for (JsonNode itemNode : itemsNode) {
					RoomEntity room = parseRoom(itemNode);
					rooms.add(room);
				}

				return CompletableFuture.completedFuture(rooms);
			}

		} catch (URISyntaxException exception) {
			throw new RoomException(RoomErrorCode.ERROR_URI);
		} catch (JsonProcessingException exception) {
			throw new RoomException(RoomErrorCode.JSON_PARSE_ERROR);
		}
		return CompletableFuture.completedFuture(Collections.emptyList());
	}

	private RoomEntity parseRoom(JsonNode itemNode) {

		AccommodationEntity accommodation = accommodationRepository.findById(
			itemNode.path("contentid").asLong()).orElseThrow();

		String roomImg1 = itemNode.path("roomimg1").asText();
		String roomImg2 = itemNode.path("roomimg2").asText();
		String roomImg3 = itemNode.path("roomimg3").asText();
		String roomImg4 = itemNode.path("roomimg4").asText();
		String roomImg5 = itemNode.path("roomimg5").asText();

		RoomEntity room = RoomEntity.builder()
			.id(itemNode.path("roomcode").asLong())
			.accommodation(accommodation)
			.roomTitle(itemNode.path("roomtitle").asText())
			.roomSize(itemNode.path("roomsize1").asInt())
			.roomCount(itemNode.path("roomcount").asInt())
			.roomBaseCount(itemNode.path("roombasecount").asInt())
			.roomMaxCount(itemNode.path("roommaxcount").asInt())
			.roomOffseasonMinfee1(itemNode.path("roomoffseasonminfee1").asInt())
			.roomOffseasonMinfee2(itemNode.path("roomoffseasonminfee2").asInt())
			.roomPeakseasonMinfee1(itemNode.path("roompeakseasonminfee1").asInt())
			.roomPeakseasonMinfee2(itemNode.path("roompeakseasonminfee2").asInt())
			.roomIntro(itemNode.path("roomintro").asText())
			.roomBath(itemNode.path("roombath").asText())
			.roomHometheater(itemNode.path("roomhometheater").asText())
			.roomAircondition(itemNode.path("roomaircondition").asText())
			.roomTv(itemNode.path("roomtv").asText())
			.roomPc(itemNode.path("roompc").asText())
			.roomCable(itemNode.path("roomcable").asText())
			.roomInternet(itemNode.path("roominternet").asText())
			.roomRefrigerator(itemNode.path("roomrefrigerator").asText())
			.roomToiletries(itemNode.path("roomtoiletries").asText())
			.roomSofa(itemNode.path("roomsofa").asText())
			.roomCook(itemNode.path("roomcook").asText())
			.roomTable(itemNode.path("roomtable").asText())
			.roomHairdryer(itemNode.path("roomhairdryer").asText())
			.roomImg1(roomImg1.isEmpty() ? "https://cdn.visitkorea.or.kr/img/call?cmd=VIEW&id=8c98c619-2970-4779-b86f-9bac9df9fa8b" : roomImg1)
			.roomImg2(roomImg2.isEmpty() ? "https://cdn.visitkorea.or.kr/img/call?cmd=VIEW&id=9802beb1-c944-4b30-ac3c-75f42dd16d9d" : roomImg2)
			.roomImg3(roomImg3.isEmpty() ? "https://cdn.visitkorea.or.kr/img/call?cmd=VIEW&id=015f2ac6-d152-47ab-a500-8525e3eafac4" : roomImg3)
			.roomImg4(roomImg4.isEmpty() ? "https://cdn.visitkorea.or.kr/img/call?cmd=VIEW&id=d1fb95bd-40c2-41fc-b2a6-a8c71fb8b08b" : roomImg4)
			.roomImg5(roomImg5.isEmpty() ? "https://cdn.visitkorea.or.kr/img/call?cmd=VIEW&id=3167c790-0aec-4541-b44b-eebb12fba2c4" : roomImg5)
			.build();

//		Instant now = Instant.now();
//		room.setCheckIn(now);
//		room.setCheckOut(now.plusSeconds(3600));

		return room;
	}

}
