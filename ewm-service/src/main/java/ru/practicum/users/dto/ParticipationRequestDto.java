package ru.practicum.users.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.users.model.RequestStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {

    @NotNull
    @Positive
    private Long id;

    @NotNull
    @Positive
    private Long requester;

    @NotNull
    @Positive
    private Long event;

    @NotNull
    private RequestStatus status;

    @NotNull
    private LocalDateTime created;
}