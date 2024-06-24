package com.group6.accommodation.domain.room.model.dto;

import com.group6.accommodation.domain.room.annotation.CheckOutAfterOrEqualCheckIn;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Data
@NoArgsConstructor
@CheckOutAfterOrEqualCheckIn(message = "체크아웃은 체크인 이후여야 합니다.")
public class AvailableRoomsReq {

	@NotNull
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate checkIn;

	@NotNull
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate checkOut;
}
