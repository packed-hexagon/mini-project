package com.group6.accommodation.domain.room.validator;

import com.group6.accommodation.domain.room.annotation.CheckOutAfterOrEqualCheckIn;
import com.group6.accommodation.domain.room.model.dto.AvailableRoomsReq;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoomPeriodValidator implements
	ConstraintValidator<CheckOutAfterOrEqualCheckIn, AvailableRoomsReq> {

	@Override
	public void initialize(CheckOutAfterOrEqualCheckIn constraintAnnotation) {
	}

	@Override
	public boolean isValid(AvailableRoomsReq req, ConstraintValidatorContext context) {
		return req.getCheckOut().isAfter(req.getCheckIn())
			|| req.getCheckOut().isEqual(req.getCheckIn());
	}
}
