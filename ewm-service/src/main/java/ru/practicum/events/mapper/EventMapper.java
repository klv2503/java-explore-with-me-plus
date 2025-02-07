package ru.practicum.events.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.category.mapper.CategoryDtoMapper;
import ru.practicum.category.model.Category;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.dto.UpdateEventUserRequest;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
import ru.practicum.users.dto.UserShortDto;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {
    public static NewEventDto toNewEventDto(Event event) {
        return new NewEventDto(
                event.getId(),
                event.getAnnotation(),
                Math.toIntExact(event.getCategory().getId()),
                event.getDescription(),
                event.getEventDate().toString(),
                event.getLocation(),
                event.isPaid(),
                event.getParticipantLimit(),
                event.isRequestModeration(),
                event.getTitle()
        );
    }

    public static Event dtoToEvent(NewEventDto dto, User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Category category = new Category();
        category.setId((long) dto.getCategory());

        LocalDateTime eventTime = LocalDateTime.parse(dto.getEventDate(), formatter);
        return new Event(
                dto.getId(),
                dto.getAnnotation(),
                dto.getTitle(),
                category,
                dto.getDescription(),
                eventTime,
                dto.getLocation(),
                dto.isPaid(),
                dto.getParticipantLimit(),
                dto.isRequestModeration(),
                0,
                user,
                LocalDateTime.now(),
                LocalDateTime.now(),
                State.PENDING,
                0
        );
    }


    public static EventFullDto toEventFullDto(Event event, User user) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryDtoMapper.mapCategoryToDto(event.getCategory()))
                .confirmedRequests(0)
                .eventDate(event.getEventDate().toString())
                .initiator(new UserShortDto(user.getId(), user.getName()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .createdOn(event.getCreatedOn().toString())
                .description(event.getDescription())
                .location(event.getLocation())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn().toString())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                CategoryDtoMapper.mapCategoryToDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate().toString(),
                new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()),
                event.isPaid(),
                event.getTitle(),
                event.getViews()
        );
    }

//    public static Event updateDtoToEvent(UpdateEventUserRequest dto) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        Category category = new Category();
//        category.setId((long) dto.getCategory());
//
//        LocalDateTime eventTime = LocalDateTime.parse(dto.getEventDate(), formatter);
//
//        return new Event(
//                dto.getId(),
//                dto.getAnnotation(),
//                dto.getDescription(),
//                eventTime,
//                dto.getLocation(),
//                dto.isPaid(),
//                dto.getParticipantLimit(),
//                dto.isRequestModeration(),
//                dto.getStateAction(),
//                dto.getTitle()
//        );
//
//        return new Event(
//                dto.getId(),
//                dto.getAnnotation(),
//                dto.getTitle(),
//                category,
//                dto.getDescription(),
//                eventTime,
//                dto.getLocation(),
//                dto.isPaid(),
//                dto.getParticipantLimit(),
//                dto.isRequestModeration(),
//                0,
//                user,
//                LocalDateTime.now(),
//                LocalDateTime.now(),
//                State.PENDING,
//                0
//        );
//    }
}
