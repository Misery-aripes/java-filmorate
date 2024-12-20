package ru.yandex.practicum.filmorate.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateNotBefore1895Validator implements ConstraintValidator<DateNotBefore, LocalDate> {

    private LocalDate minDate;

    @Override
    public void initialize(DateNotBefore constraintAnnotation) {
        this.minDate = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null) {
            return true;
        }
        return !date.isBefore(minDate);
    }
}
