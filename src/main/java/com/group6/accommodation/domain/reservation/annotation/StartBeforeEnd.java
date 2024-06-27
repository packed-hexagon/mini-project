package com.group6.accommodation.domain.reservation.annotation;

import com.group6.accommodation.domain.reservation.annotation.validator.StartBeforeEndValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StartBeforeEndValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface StartBeforeEnd {
    String message() default "시작 날짜는 끝나는 날짜보다 과거일 수 없습니다.";
    Class[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String startTime();
    String endTime();
}
