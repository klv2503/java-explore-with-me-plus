package ru.practicum.events.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.EventStateAction;
import ru.practicum.events.model.State;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.events.validation.AdminEventValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<EventFullDto> getEvents(
            List<Long> users,
            List<String> states,
            List<Long> categories,
            String rangeStart,
            String rangeEnd,
            int from,
            int size) {
        // Реализация метода
        return null;
    }

    @Transactional
    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateRequest) {
        // 1. Найти событие
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id=" + eventId + " was not found"));

        // 2. Проверки перед обновлением
        AdminEventValidator.validateEventStatusUpdate(event, updateRequest);

        // 3. Обновление данных события
        if (updateRequest.getTitle() != null) {
            event.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getAnnotation() != null) {
            event.setAnnotation(updateRequest.getAnnotation());
        }
        if (updateRequest.getDescription() != null) {
            event.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getEventDate() != null) {
            event.setEventDate(updateRequest.getEventDate());
        }
        if (updateRequest.getCategory() != null) {
            Category category = categoryRepository
                    .findById(Long.valueOf(updateRequest.getCategory()))
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Category with id=" + updateRequest.getCategory() + " not found"));

            event.setCategory(category);
        }
        if (updateRequest.getPaid() != null) {
            event.setPaid(updateRequest.getPaid());
        }
        if (updateRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateRequest.getParticipantLimit());
        }
        if (updateRequest.getStateAction() != null) {
            updateEventState(event, updateRequest.getStateAction());
        }

        // 4. Сохранение изменений
        event = eventRepository.save(event);

        // 5. Преобразование в DTO и возврат
        return EventMapper.toEventFullDto(event, event.getInitiator());
    }

    private void updateEventState(Event event, String stateAction) {
        if (EventStateAction.valueOf(stateAction).equals(EventStateAction.PUBLISH_EVENT)) {
            event.setState(State.PUBLISHED);
        } else if (EventStateAction.valueOf(stateAction).equals(EventStateAction.REJECT_EVENT)) {
            event.setState(State.CANCELED);
        }
    }
}
