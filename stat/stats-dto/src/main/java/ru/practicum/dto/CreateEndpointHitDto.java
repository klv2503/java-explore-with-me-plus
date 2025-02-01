package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.validation.CreateEndpointHitDtoValidation;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEndpointHitDto implements CreateEndpointHitDtoValidation {
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
