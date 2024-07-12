package com.group6.accommodation.domain.accommodation.annotation.validator;

import com.group6.accommodation.domain.accommodation.annotation.ValidCategory;
import com.group6.accommodation.domain.accommodation.model.enums.Category;
import com.group6.accommodation.global.exception.error.AccommodationErrorCode;
import com.group6.accommodation.global.exception.type.AccommodationException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CategoryValidator implements ConstraintValidator<ValidCategory, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null) {
            return true;
        }
        if(Category.isValidCategoryName(value)) {
            return true;
        } else {
            throw new AccommodationException(AccommodationErrorCode.INVALID_CATEGORY);
        }
    }
}
