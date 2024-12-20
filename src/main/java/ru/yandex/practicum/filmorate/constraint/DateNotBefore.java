package ru.yandex.practicum.filmorate.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DateNotBefore1895Validator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateNotBefore {
    String message() default "The date should not be earlier than 1895-12-28";

    String value() default "1895-12-28";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}