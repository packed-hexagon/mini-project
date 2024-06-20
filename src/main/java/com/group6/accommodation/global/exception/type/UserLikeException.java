package com.group6.accommodation.global.exception.type;

import com.group6.accommodation.global.exception.error.ErrorCode;

public class UserLikeException extends CustomException{
    public UserLikeException(ErrorCode errorCode){
        super(errorCode);
    }

}
