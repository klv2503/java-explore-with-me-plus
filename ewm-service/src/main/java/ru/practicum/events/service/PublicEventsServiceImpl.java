package ru.practicum.events.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.practicum.controller.ClientController;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.LookEventDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.users.service.AdminUserService;

@Service
@Slf4j
public class PublicEventsServiceImpl implements PublicEventsService {

    private final RestClient restClient;

    private final EventRepository eventRepository;

    private final AdminUserService adminUserService;

    private final ClientController clientController;

    public PublicEventsServiceImpl(@Qualifier(value = "eventsRestClient") RestClient restClient,
                                   EventRepository eventRepository,
                                   AdminUserService adminUserService,
                                   ClientController clientController) {
        this.restClient = restClient;
        this.eventRepository = eventRepository;
        this.adminUserService = adminUserService;
        this.clientController = clientController;
    }

    @Override
    public Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event with " + id + " not found"));
    }

    @Override
    public EventFullDto getEventInfo(LookEventDto lookEventDto) {
        log.info("\nPublicEventsServiceImpl.getEventInfo: accepted id {}", lookEventDto.getId());
        Event event = getEvent(lookEventDto.getId());
        Integer views = restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/views")
                        .queryParam("addr", lookEventDto.getIp())
                        .queryParam("uri", lookEventDto.getUri())
                        .build())
                .retrieve()
                .toEntity(Integer.class)
                .getBody();
        event.setViews((views == null) ? 0 : views);
        return EventMapper.toEventFullDto(event);
    }
}