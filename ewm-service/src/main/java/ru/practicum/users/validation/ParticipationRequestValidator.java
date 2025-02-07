package ru.practicum.users.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.ParticipationRequestRepository;

@Component
@RequiredArgsConstructor
public class ParticipationRequestValidator {

    private final ParticipationRequestRepository requestRepository;

    public void validate(User user, Event event, long confirmedRequestsCount) {
        if (event.getInitiator().getId().equals(user.getId())) {
            throw new DataIntegrityViolationException("Event initiator cannot participate in their own event");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new DataIntegrityViolationException("Cannot participate in an unpublished event");
        }

        if (requestRepository.existsByUserIdAndEventId(user.getId(), event.getId())) {
            throw new DataIntegrityViolationException("User already has a participation request for this event");
        }

        if (event.getParticipantLimit() > 0 && confirmedRequestsCount >= event.getParticipantLimit()) {
            throw new DataIntegrityViolationException("Event participant limit reached");
        }
    }
}