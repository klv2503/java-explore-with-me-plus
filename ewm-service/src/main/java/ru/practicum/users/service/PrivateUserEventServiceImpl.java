package ru.practicum.users.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.config.DateConfig;
import ru.practicum.errors.ForbiddenActionException;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventUserRequest;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.StateEvent;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.users.dto.GetUserEventsDto;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PrivateUserEventServiceImpl implements PrivateUserEventService {
    EventRepository eventRepository;
    AdminUserService adminUserService;
    CategoryRepository categoryRepository;

    @Override
    public List<EventShortDto> getUsersEvents(GetUserEventsDto dto) {
        User user = adminUserService.getUser(dto.getUserId());
        PageRequest page = PageRequest.of(dto.getFrom() > 0 ? dto.getFrom() / dto.getSize() : 0, dto.getSize());
        return eventRepository.findAllByInitiatorId(user.getId(), page).stream()
                .map(EventMapper::toEventShortDto)
                .toList();
    }

    @Override
    public EventFullDto getUserEventById(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto addNewEvent(Long userId, NewEventDto eventDto) {
        User user = adminUserService.getUser(userId);
        Event event = EventMapper.dtoToEvent(eventDto, user);

        eventRepository.save(event);

        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest updateDto) {
        User user = adminUserService.getUser(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));

        Optional.ofNullable(updateDto.getTitle()).ifPresent(event::setTitle);
        Optional.ofNullable(updateDto.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(updateDto.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(updateDto.getEventDate()).map(this::parseEventDate).ifPresent(event::setEventDate);
        Optional.ofNullable(updateDto.getLocation()).ifPresent(event::setLocation);

        if (updateDto.getCategory() != 0) {
            event.setCategory(categoryRepository.findById((long) updateDto.getCategory())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + updateDto.getCategory())));
        }

        updateEventState(event, updateDto.getStateAction());

        if (!event.getState().equals(StateEvent.CANCELED)) {
            event.setPaid(updateDto.isPaid());
            event.setParticipantLimit(updateDto.getParticipantLimit());
        }
        event.setRequestModeration(updateDto.isRequestModeration());
        event.setInitiator(user);

        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    private LocalDateTime parseEventDate(String date) {
        return LocalDateTime.parse(date, DateConfig.FORMATTER);
    }

    private void updateEventState(Event event, String stateAction) {
        if (stateAction == null) return;

        if ("PUBLISH_REVIEW".equals(stateAction)) {
            throw new ForbiddenActionException("Publishing this event is forbidden.");
        }

        switch (stateAction) {
            case "CANCEL_REVIEW":
                event.setState(StateEvent.CANCELED);
                break;
            case "SEND_TO_REVIEW":
                event.setState(StateEvent.PENDING);
                break;
            default:
                throw new IllegalArgumentException("Invalid state action: " + stateAction);
        }
    }
}
