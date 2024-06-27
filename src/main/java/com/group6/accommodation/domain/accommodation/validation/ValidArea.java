package com.group6.accommodation.domain.accommodation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AreaValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidArea {

    String message() default "유효하지 않은 지역명입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
