package com.vetsync.backend.global.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = OptionMapValidator.class)
public @interface ValidOptionMap {
    String message() default "options의 키는 숫자(0-9)고 빈 숫자가 없는 오름차순이어야 하며, 값은 비어 있을 수 없습니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
