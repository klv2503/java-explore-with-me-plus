package ru.practicum.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocalDateTime convert(String source) {
        if (source.isBlank()) {
            return null;
        }

        try {
            String decodedDate = URLDecoder.decode(source, StandardCharsets.UTF_8);
            return LocalDateTime.parse(decodedDate, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + source, e);
        }
    }
}
