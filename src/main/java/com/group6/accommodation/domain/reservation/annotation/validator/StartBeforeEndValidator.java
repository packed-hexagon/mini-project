package com.group6.accommodation.domain.reservation.annotation.validator;

import com.group6.accommodation.domain.reservation.annotation.StartBeforeEnd;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, Object> {

    private String startTime;
    private String endTime;

    @Override
    public void initialize(StartBeforeEnd constraintAnnotation) {
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

            boolean isValid = start.isBefore(end);
            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(startTime)
                    .addConstraintViolation();
            }

            return isValid;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
    }
}
