package ru.practicum.events.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.ap.internal.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.practicum.config.DateConfig;
import ru.practicum.config.StatsClientConfig;
import ru.practicum.controller.ClientController;
import ru.practicum.dto.ReadEndpointHitDto;
import ru.practicum.errors.EventNotPublishedException;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.LookEventDto;
import ru.practicum.events.dto.SearchEventsParams;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.QEvent;
import ru.practicum.events.model.StateEvent;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.users.model.ParticipationRequestStatus;
import ru.practicum.users.service.ParticipationRequestService;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicEventsServiceImpl implements PublicEventsService {

    private final EventRepository eventRepository;

    private final ClientController clientController;

    private final ParticipationRequestService participationRequestService;

    @Autowired
    public PublicEventsServiceImpl(EventRepository eventRepository, ParticipationRequestService participationRequestService,
                                   StatsClientConfig statsClientConfig) {
        this.eventRepository = eventRepository;
        this.participationRequestService = participationRequestService;
        this.clientController = new ClientController(statsClientConfig.getHost(), statsClientConfig.getPort());
    }

    @Override
    public Event getEvent(Long id) {
        return eventRepository.findEventWithStatus(id, ParticipationRequestStatus.CONFIRMED);
    }

    @Override
    public int getEventsViews(long id, LocalDateTime publishedOn) {
        List<String> uris = List.of(URLEncoder.encode("/events/" + id));
        List<ReadEndpointHitDto> res = clientController.getHits(publishedOn.format(DateConfig.FORMATTER),
                LocalDateTime.now().format(DateConfig.FORMATTER), uris, false);
        log.info("\nPublicEventsServiceImpl.getEventsViews: res {}", res);
        return res.getFirst().getHits();
    }

    public List<Event> getEventsByListIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids))
            return List.of();

        List<Event> events = eventRepository.findEventsWithConfirmedCount(ids);
        if (CollectionUtils.isEmpty(events))
            return events;

        LocalDateTime start = events.stream()
                .map(Event::getPublishedOn)
                .min(LocalDateTime::compareTo)
                .orElseThrow(() ->
                        new RuntimeException("Internal server error during execution PublicEventsServiceImpl"));
        List<String> uris = events.stream()
                .map(event -> URLEncoder.encode("/event/" + event.getId()))
                .toList();

        List<ReadEndpointHitDto> acceptedList = clientController.getHits(start.format(DateConfig.FORMATTER),
                LocalDateTime.now().format(DateConfig.FORMATTER), uris, false);
        // Заносим значения views в список events
        viewsToEvents(acceptedList, events);
        return events;
    }

    @Override
    public EventFullDto getEventInfo(LookEventDto lookEventDto) {
        log.info("\nPublicEventsServiceImpl.getEventInfo: accepted {}", lookEventDto);
        Event event = getEvent(lookEventDto.getId());
        log.info("\nPublicEventsServiceImpl.getEventsViews: event {}", event);
        if (!event.getState().equals(StateEvent.PUBLISHED)) {
            throw new EventNotPublishedException("There is no published event id " + event.getId());
        }
        // Получаем views
        event.setViews(getEventsViews(event.getId(), event.getPublishedOn()));
        //Имеем новый просмотр - сохраняем его
        clientController.saveView(lookEventDto.getIp(), lookEventDto.getUri());

        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getFilteredEvents(SearchEventsParams searchEventsParams, LookEventDto lookEventDto) {

        BooleanBuilder builder = new BooleanBuilder();

        // Добавляем условия отбора по контексту
        if (!Strings.isEmpty(searchEventsParams.getText())) {
            builder.or(QEvent.event.annotation.containsIgnoreCase(searchEventsParams.getText()))
                    .or(QEvent.event.description.containsIgnoreCase(searchEventsParams.getText()));
        }

        // Добавляем отбор по статусу PUBLISHED
        builder.and(QEvent.event.state.eq(StateEvent.PUBLISHED));
        // ... и по списку категорий
        if (!CollectionUtils.isEmpty(searchEventsParams.getCategories()))
            builder.and(QEvent.event.category.id.in(searchEventsParams.getCategories()));
        // ... и еще по признаку платные/бесплатные
        if (searchEventsParams.getPaid() != null)
            builder.and(QEvent.event.paid.eq(searchEventsParams.getPaid()));

        // Добавляем условие диапазона дат
        LocalDateTime start;
        LocalDateTime end;
        if (searchEventsParams.getRangeStart() == null) {
            start = LocalDateTime.now();
            searchEventsParams.setRangeStart(start.format(DateConfig.FORMATTER));
        } else {
            start = LocalDateTime.parse(searchEventsParams.getRangeStart(), DateConfig.FORMATTER);
        }
        if (searchEventsParams.getRangeEnd() == null) {
            builder.and(QEvent.event.eventDate.goe(start));
        } else {
            end = LocalDateTime.parse(searchEventsParams.getRangeEnd(), DateConfig.FORMATTER);
            builder.and(QEvent.event.eventDate.between(start, end));
        }

        List<Event> events = eventRepository.searchEvents(builder, ParticipationRequestStatus.CONFIRMED,
                searchEventsParams.getOnlyAvailable(), searchEventsParams.getFrom(), searchEventsParams.getSize());
        if (events.isEmpty())
            return List.of();

        log.info("PublicEventsServiceImpl.getFilteredEvents: events {}", events);
        // Если не было установлено rangeEnd, устанавливаем
        if (searchEventsParams.getRangeEnd() == null) {
            searchEventsParams.setRangeEnd(LocalDateTime.now().format(DateConfig.FORMATTER));
        }
        // Формируем список uris
        List<String> uris = new ArrayList<>();
        for (Event e : events) {
            uris.add(URLEncoder.encode("/events/" + e.getId()));
        }

        List<ReadEndpointHitDto> acceptedList = clientController.getHits(searchEventsParams.getRangeStart(),
                searchEventsParams.getRangeEnd(), uris, false);
        viewsToEvents(acceptedList, events);

        // Сортировка. Для начала проверяем значение параметра сортировки
        String sortParam;
        if (Strings.isEmpty(searchEventsParams.getSort())) {
            sortParam = "VIEWS";
        } else {
            sortParam = searchEventsParams.getSort().toUpperCase();
        }
        // Дополняем сортировкой
        List<Event> sortedEvents = new ArrayList<>();
        if (sortParam.equalsIgnoreCase("EVENT_DATE")) {
            sortedEvents = events.stream()
                    .sorted(Comparator.comparing(Event::getEventDate)) // Сортируем по eventDate
                    .toList();
        } else {
            sortedEvents = events.stream()
                    .sorted(Comparator.comparingInt(Event::getViews).reversed()) // Сортируем по views
                    .toList();
        }

        clientController.saveView(lookEventDto.getIp(), lookEventDto.getUri());
        log.info("\n Final list {}", sortedEvents);
        return EventMapper.toListEventShortDto(sortedEvents);
    }

    public void viewsToEvents(List<ReadEndpointHitDto> viewsList, List<Event> events) {
        // Заносим значения views в список events
        Map<Integer, Integer> workMap = new HashMap<>();
        for (ReadEndpointHitDto r : viewsList) {
            int i = Integer.parseInt(r.getUri().substring(r.getUri().lastIndexOf("/") + 1));
            workMap.put(i, r.getHits());
        }
        for (Event e : events) {
            e.setViews(workMap.getOrDefault(e.getId(), 0));
        }
    }
}