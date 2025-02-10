package ru.practicum.events.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.controller.ClientController;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.LookEventDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.users.service.AdminUserService;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicEventsServiceImpl implements PublicEventsService {

    private final EventRepository eventRepository;

    private final AdminUserService adminUserService;

    private final ClientController clientController;

    @Override
    public Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event with " + id + " not found"));
    }

    @Override
    public EventFullDto getEventInfo(LookEventDto lookEventDto) {
        log.info("\nPublicEventsServiceImpl.getEventInfo: accepted id {}", lookEventDto.getId());
        Event event = getEvent(lookEventDto.getId());
        //Имеем новый просмотр - сохраняем его
        clientController.saveView(lookEventDto.getIp(), lookEventDto.getUri());

        //Считаем количество просмотров
        Integer views = clientController.countView(lookEventDto.getUri());
        event.setViews((views == null) ? 0 : views);
        return EventMapper.toEventFullDto(event);
    }
}