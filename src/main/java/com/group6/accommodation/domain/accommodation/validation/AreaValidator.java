package com.group6.accommodation.domain.accommodation.validation;

import com.group6.accommodation.domain.accommodation.model.enums.Area;
import com.group6.accommodation.global.exception.type.AccommodationException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AreaValidator implements ConstraintValidator<ValidArea, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            return true;
        }
        try {
            Area.getCodeByName(value);
            return true;
        } catch(AccommodationException e) {
            return false;
        }
    }
}
