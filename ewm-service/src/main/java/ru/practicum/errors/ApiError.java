package ru.practicum.errors;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ApiError {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    List<String> errors;
    String message;
    String reason;
    String status;
    String timestamp;

    public ApiError(String status, String reason, Throwable ex) {
        this.errors = acceptStackTrace(ex);
        this.message = ex.getMessage();
        this.reason = reason;
        this.status = status;
        this.timestamp = LocalDateTime.now().format(FORMATTER);
    }

    private List<String> acceptStackTrace(Throwable ex) {
        return List.of(ex.getStackTrace()).stream()
                .map(StackTraceElement::toString)
                .collect(Collectors.toList());
    }
}
