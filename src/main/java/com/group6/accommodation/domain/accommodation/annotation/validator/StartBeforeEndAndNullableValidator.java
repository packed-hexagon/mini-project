package com.group6.accommodation.domain.accommodation.annotation.validator;

import com.group6.accommodation.domain.accommodation.annotation.StartBeforeEndAndNullable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDate;

public class StartBeforeEndAndNullableValidator implements ConstraintValidator<StartBeforeEndAndNullable, Object> {

    private String startTime;
    private String endTime;


    @Override
    public void initialize(StartBeforeEndAndNullable constraintAnnotation) {
        this.startTime = constraintAnnotation.startTime();
        this.endTime = constraintAnnotation.endTime();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            // 필드 접근을 위한 리플렉션 사용
            Field startTimeField = value.getClass().getDeclaredField(startTime);
            Field endTimeField = value.getClass().getDeclaredField(endTime);

            // 접근 제어 해제
            startTimeField.setAccessible(true);
            endTimeField.setAccessible(true);

            LocalDate start = (LocalDate) startTimeField.get(value);
            LocalDate end = (LocalDate) endTimeField.get(value);

            if(start == null && end == null) {
                return true;
            }

            boolean isValid = start.isBefore(end);
            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode(startTime)
                        .addConstraintViolation();
            }

            return isValid;
        } catch (Exception e) {
            return false;
        }
    }
}