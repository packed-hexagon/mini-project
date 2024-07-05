package com.group6.accommodation.domain.accommodation.annotation.validator;

import com.group6.accommodation.domain.accommodation.annotation.ValidArea;
import com.group6.accommodation.domain.accommodation.model.enums.Area;
import com.group6.accommodation.global.exception.error.AccommodationErrorCode;
import com.group6.accommodation.global.exception.type.AccommodationException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class AreaValidator implements ConstraintValidator<ValidArea, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null 값 허용
        }
        if(Area.isValidAreaName(value)) {
            return true;
        } else {
            throw new AccommodationException(AccommodationErrorCode.INVALID_AREA);
        }
    }
}
