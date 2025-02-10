package ru.practicum.events.service;

import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.LookEventDto;
import ru.practicum.events.model.Event;

public interface PublicEventsService {

    Event getEvent(Long id);

    EventFullDto getEventInfo(LookEventDto lookEventDto);

}
