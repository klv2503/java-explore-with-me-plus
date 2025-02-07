package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.events.model.Location;
import ru.practicum.events.model.State;
import ru.practicum.users.dto.UserShortDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventFullDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String annotation;
    private CategoryDto category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdOn;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String eventDate;
    private UserShortDto initiator;
    private Location location;
    private boolean paid;
    private int participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String publishedOn;
    private boolean requestModeration;
    private State state;
    private String title;
    private int views;
}
