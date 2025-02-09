package ru.practicum.events.validation;

import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventStateAction;
import ru.practicum.events.model.State;

import java.time.LocalDateTime;

public class AdminEventValidator {
    public static void validateEventStatusUpdate(Event event, UpdateEventAdminRequest updateRequest) {
        LocalDateTime eventDate = updateRequest.getEventDate();

        if (updateRequest.getEventDate() != null && eventDate.isBefore(event.getCreatedOn().plusHours(1))) {
            throw new IllegalStateException("Event start time must be at least 1 hour after publication.");
        }

        boolean isPublishAction = EventStateAction.valueOf(updateRequest.getStateAction())
                .equals(EventStateAction.PUBLISH_EVENT);
        boolean isRejectAction = EventStateAction.valueOf(updateRequest.getStateAction())
                .equals(EventStateAction.PUBLISH_EVENT);

        if (isPublishAction && !event.getState().equals(State.PENDING)) {
            throw new IllegalStateException("Cannot publish event. It must be in PENDING state.");
        }

        if (isRejectAction && event.getState().equals(State.PUBLISHED)) {
            throw new IllegalStateException("Cannot reject event. It is already published.");
        }
    }
}
