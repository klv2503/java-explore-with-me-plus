package ru.practicum.users.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.users.dto.ParticipationRequestDto;
import ru.practicum.users.mapper.ParticipationRequestToDtoMapper;
import ru.practicum.users.model.ParticipationRequest;
import ru.practicum.users.model.RequestStatus;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.ParticipationRequestRepository;
import ru.practicum.users.repository.UserRepository;
import ru.practicum.users.validation.ParticipationRequestValidator;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    private final ParticipationRequestValidator participationRequestValidator;

    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id=" + userId + " was not found"));

        return requestRepository.findByUserId(userId)
                .stream()
                .map(ParticipationRequestToDtoMapper::mapToDto)
                .toList();
    }

    @Transactional
    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id=" + userId + " was not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id=" + eventId + " was not found"));

        participationRequestValidator.validate(user, event);

        ParticipationRequest request = new ParticipationRequest();
        request.setUser(user);
        request.setEvent(event);
        request.setStatus(event.isRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED);
        request.setCreated(LocalDateTime.now());

        ParticipationRequest savedRequest = requestRepository.save(request);
        return ParticipationRequestToDtoMapper.mapToDto(savedRequest);
    }

    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findByIdAndUserId(requestId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Request with id=" + requestId + " was not found"));

        request.setStatus(RequestStatus.CANCELED);
        requestRepository.save(request);

        return ParticipationRequestToDtoMapper.mapToDto(request);
    }
}