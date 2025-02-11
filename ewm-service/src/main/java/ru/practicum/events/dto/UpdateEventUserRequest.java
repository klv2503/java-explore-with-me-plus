package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.config.DateConfig;
import ru.practicum.events.model.Location;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEventUserRequest {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String annotation;
    private int category;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateConfig.FORMAT)
    private String eventDate;
    private Location location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private String stateAction;
    private String title;
}
