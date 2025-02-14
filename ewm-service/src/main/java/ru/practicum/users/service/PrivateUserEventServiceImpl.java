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
import ru.practicum.users.dto.EventRequestStatusUpdateRequest;
import ru.practicum.users.dto.GetUserEventsDto;
import ru.practicum.users.dto.ParticipationRequestDto;
import ru.practicum.users.mapper.ParticipationRequestToDtoMapper;
import ru.practicum.users.dto.EventRequestStatusUpdateResult;
import ru.practicum.users.model.ParticipationRequest;
import ru.practicum.users.model.ParticipationRequestStatus;
import ru.practicum.users.model.RequestStatus;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.ParticipationRequestRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PrivateUserEventServiceImpl implements PrivateUserEventService {
    private EventRepository eventRepository;
    private AdminUserService adminUserService;
    private CategoryRepository categoryRepository;
    private ParticipationRequestRepository requestRepository;

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
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId + " for user " + userId));
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

        event.setRequestModeration(updateDto.isRequestModeration());
        event.setInitiator(user);

        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<ParticipationRequestDto> getUserEventRequests(Long userId, Long eventId) {
        eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId + " for user " + userId));
        return requestRepository.findByEventId(eventId).stream()
                .map(ParticipationRequestToDtoMapper::mapToDto)
                .toList();
    }

    @Override
    public EventRequestStatusUpdateResult updateUserEventRequest(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        User user = adminUserService.getUser(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));

        List<ParticipationRequest> participation = requestRepository.findByIds(request.getRequestIds());
        for(ParticipationRequest req : participation) {
            if (!req.getStatus().equals(ParticipationRequestStatus.PENDING)) {
                throw new ForbiddenActionException("request status should be PENDING");
            }
        }

        int partLimit = event.getParticipantLimit();
        if (partLimit >= request.getRequestIds().size()) {
            requestRepository.updateStatusByIds(ParticipationRequestStatus.valueOf(request.getStatus()), request.getRequestIds());
            if (RequestStatus.valueOf(request.getStatus()).equals(RequestStatus.CONFIRMED)) {
                event.setParticipantLimit(partLimit - request.getRequestIds().size());
                eventRepository.save(event);

                for(ParticipationRequest req : participation) {
                    req.setStatus(ParticipationRequestStatus.CONFIRMED);
                }

                return EventRequestStatusUpdateResult.builder()
                        .confirmedRequests(participation.stream()
                                .map(ParticipationRequestToDtoMapper::mapToDto).toList())
                        .build();
            } else {
                event.setParticipantLimit(partLimit - request.getRequestIds().size());
                eventRepository.save(event);

                for(ParticipationRequest req : participation) {
                    req.setStatus(ParticipationRequestStatus.REJECTED);
                }

                return EventRequestStatusUpdateResult.builder()
                        .rejectedRequests(participation.stream()
                                .map(ParticipationRequestToDtoMapper::mapToDto).toList())
                        .build();
            }
        }
        else  if (partLimit == 0) {
            throw new ForbiddenActionException("Participation limit is 0");
        }
        else {
            List<Long> confirmed = new ArrayList<>();
            List<Long> rejected = new ArrayList<>();
            for (int i = 1; i <= request.getRequestIds().size(); i++) {
                if (i > partLimit) {
                    rejected.add(request.getRequestIds().get(i));
                }
                else confirmed.add(request.getRequestIds().get(i));
            }
            requestRepository.updateStatusByIds(ParticipationRequestStatus.CONFIRMED, confirmed);
            requestRepository.updateStatusByIds(ParticipationRequestStatus.REJECTED, rejected);

            EventRequestStatusUpdateResult res = new EventRequestStatusUpdateResult();

            for(ParticipationRequest req : participation) {
                for (Long id : confirmed) {
                    if (Objects.equals(req.getId(), id)) {
                        req.setStatus(ParticipationRequestStatus.CONFIRMED);
                    }
                }
                req.setStatus(ParticipationRequestStatus.CANCELED);
            }

            List<ParticipationRequest> updatedRequestsConfirmed = participation.stream()
                    .filter(req -> confirmed.contains(req.getId()))
                    .peek(req -> req.setStatus(ParticipationRequestStatus.CONFIRMED))
                    .toList();

            List<ParticipationRequest> updatedRequestsRejected = participation.stream()
                    .filter(req -> rejected.contains(req.getId()))
                    .peek(req -> req.setStatus(ParticipationRequestStatus.REJECTED))
                    .toList();

            res.setConfirmedRequests(updatedRequestsConfirmed.stream()
                    .map(ParticipationRequestToDtoMapper::mapToDto).toList());
            res.setRejectedRequests(updatedRequestsRejected.stream()
                    .map(ParticipationRequestToDtoMapper::mapToDto).toList());

            event.setParticipantLimit(0);
            eventRepository.save(event);

            return res;
        }
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
