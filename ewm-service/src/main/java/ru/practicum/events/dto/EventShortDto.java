package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.users.dto.UserShortDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventShortDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String annotation;
    private CategoryDto category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String eventDate;
    private UserShortDto initiator;
    private boolean paid;
    private String title;
}
