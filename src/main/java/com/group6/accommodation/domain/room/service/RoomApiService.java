package com.group6.accommodation.domain.room.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group6.accommodation.domain.accommodation.model.entity.AccommodationEntity;
import com.group6.accommodation.domain.accommodation.repository.AccommodationRepository;
import com.group6.accommodation.domain.room.config.OpenapiConfig;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RoomApiService {

	private final AccommodationRepository accommodationRepository;
	private final ObjectMapper objectMapper;
	private final RestTemplate restTemplate;
	private final OpenapiConfig openapiConfig;

	private static final int NUM_OF_ROWS = 10;

	// 토탈 카운트 = 객실 갯수
	@Cacheable("rooms")
	public List<RoomEntity> fetchAllRooms() {
		try {
			int totalCount = getTotalCount();
			int totalPages = (int) Math.ceil((double) totalCount / NUM_OF_ROWS);
			int testPages = 10;

			List<CompletableFuture<List<RoomEntity>>> futures = new ArrayList<>();
			for (int pageNo = 1; pageNo <= testPages; pageNo++) {
				futures.add(fetchRoomsAsync(pageNo));
			}

			return futures.stream()
				.map(CompletableFuture::join)
				.flatMap(List::stream)
				.collect(Collectors.toList());
		} catch (Exception e) {
			throw new RoomException(RoomErrorCode.NOT_FOUND_DATA_PAGE);
		}
	}

	private int getTotalCount() throws URISyntaxException, JsonProcessingException {
		try {
			URI uri = new URI(
				openapiConfig.getBaseUrlRoom() + "?serviceKey=" + openapiConfig.getApiKey()
					+ "&numOfRows=1&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json");
			ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

			HttpStatusCode statusCode = responseEntity.getStatusCode();

			if (statusCode == HttpStatus.OK) {
				JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());

				return rootNode.path("response").path("body").path("totalCount").asInt();
			}
		} catch (URISyntaxException e) {
			throw new RoomException(RoomErrorCode.ERROR_URI);
		} catch (RestClientException e) {
			throw new RoomException(RoomErrorCode.ERROR_RESTTEMPLATE);
		}
		return 0;
	}

	@Async
	@Retryable(value = RestClientException.class, maxAttempts = 3)
	public CompletableFuture<List<RoomEntity>> fetchRoomsAsync(int pageNo) {
		try {
			URI uri = new URI(openapiConfig.getBaseUrlRoom() + "?serviceKey=" + openapiConfig.getApiKey() + "&numOfRows=" + NUM_OF_ROWS + "&pageNo=" + pageNo + "&MobileOS=AND&MobileApp=TestApp&_type=json");
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

			return CompletableFuture.completedFuture(Collections.emptyList());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private RoomEntity parseRoom(JsonNode itemNode) {
		RoomEntity room = new RoomEntity();
		AccommodationEntity accommodation = accommodationRepository.findById(
			itemNode.path("contentid").asLong()).orElseThrow();

		room.setRoomId(itemNode.path("roomcode").asLong());
		room.setAccommodation(accommodation);
		room.setRoomTitle(itemNode.path("roomtitle").asText());
		room.setRoomSize(itemNode.path("roomsize1").asInt());
		room.setRoomCount(itemNode.path("roomcount").asInt());
		room.setRoomBaseCount(itemNode.path("roombasecount").asInt());
		room.setRoomMaxCount(itemNode.path("roommaxcount").asInt());
		room.setRoomOffseasonMinfee1(itemNode.path("roomoffseasonminfee1").asInt());
		room.setRoomOffseasonMinfee2(itemNode.path("roomoffseasonminfee2").asInt());
		room.setRoomPeakseasonMinfee1(itemNode.path("roompeakseasonminfee1").asInt());
		room.setRoomPeakseasonMinfee2(itemNode.path("roompeakseasonminfee2").asInt());
		room.setRoomIntro(itemNode.path("roomintro").asText());
		room.setRoomBath(itemNode.path("roombath").asText());
		room.setRoomHometheater(itemNode.path("roomhometheater").asText());
		room.setRoomAircondition(itemNode.path("roomaircondition").asText());
		room.setRoomTv(itemNode.path("roomtv").asText());
		room.setRoomPc(itemNode.path("roompc").asText());
		room.setRoomCable(itemNode.path("roomcable").asText());
		room.setRoomInternet(itemNode.path("roominternet").asText());
		room.setRoomRefrigerator(itemNode.path("roomrefrigerator").asText());
		room.setRoomToiletries(itemNode.path("roomtoiletries").asText());
		room.setRoomSofa(itemNode.path("roomsofa").asText());
		room.setRoomCook(itemNode.path("roomcook").asText());
		room.setRoomTable(itemNode.path("roomtable").asText());
		room.setRoomHairdryer(itemNode.path("roomhairdryer").asText());

		String roomImg1 = itemNode.path("roomimg1").asText();
		room.setRoomImg1(roomImg1.isEmpty() ? "https://cdn.visitkorea.or.kr/img/call?cmd=VIEW&id=8c98c619-2970-4779-b86f-9bac9df9fa8b" : roomImg1);
		String roomImg2 = itemNode.path("roomimg2").asText();
		room.setRoomImg2(roomImg2.isEmpty() ? "https://cdn.visitkorea.or.kr/img/call?cmd=VIEW&id=9802beb1-c944-4b30-ac3c-75f42dd16d9d" : roomImg2);
		String roomImg3 = itemNode.path("roomimg3").asText();
		room.setRoomImg3(roomImg3.isEmpty() ? "https://cdn.visitkorea.or.kr/img/call?cmd=VIEW&id=015f2ac6-d152-47ab-a500-8525e3eafac4" : roomImg3);
		String roomImg4 = itemNode.path("roomimg4").asText();
		room.setRoomImg4(roomImg4.isEmpty() ? "https://cdn.visitkorea.or.kr/img/call?cmd=VIEW&id=d1fb95bd-40c2-41fc-b2a6-a8c71fb8b08b" : roomImg4);
		String roomImg5 = itemNode.path("roomimg5").asText();
		room.setRoomImg5(roomImg5.isEmpty() ? "https://cdn.visitkorea.or.kr/img/call?cmd=VIEW&id=3167c790-0aec-4541-b44b-eebb12fba2c4" : roomImg5);

		return room;
	}

}
