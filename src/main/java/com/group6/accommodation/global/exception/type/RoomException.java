package com.group6.accommodation.global.exception.type;

import com.group6.accommodation.global.exception.error.ErrorCode;

public class RoomException extends CustomException{

	public RoomException(ErrorCode errorCode) {
		super(errorCode);
	}
}
