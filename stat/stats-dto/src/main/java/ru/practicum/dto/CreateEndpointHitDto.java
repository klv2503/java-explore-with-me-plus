package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.validation.CreateEndpointHitDtoValidation;

import java.time.LocalDateTime;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEndpointHitDto implements CreateEndpointHitDtoValidation {
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;

    @Override
    public String getApp() {
        return app;
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
