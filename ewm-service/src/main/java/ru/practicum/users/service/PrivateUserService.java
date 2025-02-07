package ru.practicum.users.service;

import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.users.dto.GetUserEventsDto;

import java.util.List;

public interface PrivateUserService {
    List<EventShortDto> getUsersEvents(GetUserEventsDto dto);
    EventFullDto addNewEvent(Long userId, NewEventDto eventDto);
}
