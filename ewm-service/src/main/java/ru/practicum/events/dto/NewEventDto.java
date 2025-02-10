package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.config.DateConfig;
import ru.practicum.events.model.Location;
import ru.practicum.events.validation.TimeNotEarly;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewEventDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private int category;

    @NotNull
    @NotBlank
    @Size(min = 70, max = 2000)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateConfig.FORMAT)
    @TimeNotEarly(hours = 2, message = "Время не должно быть ранее чем через 2 часа.")
    private String eventDate;

    @NotNull
    private Location location;

    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}
