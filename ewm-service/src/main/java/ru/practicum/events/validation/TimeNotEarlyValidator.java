package ru.practicum.events.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeNotEarlyValidator implements ConstraintValidator<TimeNotEarly, String> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private int hours;

    @Override
    public void initialize(TimeNotEarly constraintAnnotation) {
        this.hours = constraintAnnotation.hours();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        try {
            LocalDateTime appointedDate = LocalDateTime.parse(value, FORMATTER);
            LocalDateTime minDate = LocalDateTime.now().plusHours(hours);
            return !appointedDate.isBefore(minDate);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}