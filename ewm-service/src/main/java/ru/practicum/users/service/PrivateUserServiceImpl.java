package ru.practicum.users.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.users.dto.GetUserEventsDto;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.AdminUserRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PrivateUserServiceImpl implements PrivateUserService {
    EventRepository eventRepository;
    AdminUserRepository adminUserRepository;

    @Override
    public List<EventShortDto> getUsersEvents(GetUserEventsDto dto) {
        User user = adminUserRepository.findById(dto.getUserId()).orElseThrow();
        PageRequest page = PageRequest.of(dto.getFrom() > 0 ? dto.getFrom() / dto.getSize() : 0, dto.getSize());
        return eventRepository.findAllByInitiatorId(dto.getUserId(), page).stream()
                .map(EventMapper::toEventShortDto)
                .toList();
    }

    @Override
    public EventFullDto addNewEvent(Long userId, NewEventDto eventDto) {
        User user = adminUserRepository.findById(userId).orElseThrow();
        Event event = EventMapper.dtoToEvent(eventDto, user);

        eventRepository.save(event);

        return EventMapper.toEventFullDto(event, user);
    }
}
