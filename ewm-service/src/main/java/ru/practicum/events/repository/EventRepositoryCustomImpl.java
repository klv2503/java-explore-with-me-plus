package ru.practicum.events.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.QEvent;
import ru.practicum.users.model.ParticipationRequestStatus;
import ru.practicum.users.model.QParticipationRequest;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventRepositoryCustomImpl implements EventRepositoryCustom {
    private final EntityManager em;

    @Override
    public Page<Event> findAllWithBuilder(BooleanBuilder builder, Pageable pageable) {
        QEvent event = QEvent.event;

        // Запрос для получения данных с пагинацией и сортировкой
        JPAQuery<Event> query = new JPAQuery<>(em)
                .select(event)
                .from(event)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        List<Event> content = query.fetch();

        // Запрос для подсчета общего количества элементов (используем count)
        Long total = new JPAQuery<>(em).select(event.count())
                .from(event)
                .fetchOne();

        if (total == null)
            throw new RuntimeException("Не удалось определить количество событий");

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Event findEventWithStatus(Long eventId, ParticipationRequestStatus status) {
        QEvent event = QEvent.event;
        QParticipationRequest request = QParticipationRequest.participationRequest;

        JPAQuery<Tuple> query = new JPAQuery<>(em)
                .select(event, request.id.count())
                .from(event)
                .leftJoin(request).on(request.event.eq(event).and(request.status.eq(status)))
                .where(event.id.eq(eventId))
                .groupBy(event);
        Tuple result = query.fetchOne();

        if (result == null) {
            throw new EntityNotFoundException("Event with" + eventId + " not found");
        }

        Event foundEvent = result.get(event);
        Integer confirmedCount = result.get(request.id.count()).intValue();

        foundEvent.setConfirmedRequests((confirmedCount == null) ? 0 : confirmedCount);

        return foundEvent;
    }

    @Override
    public List<Event> searchEvents(BooleanBuilder eventCondition, ParticipationRequestStatus status,
                                    boolean onlyAvailable, int from, int size) {
        QEvent event = QEvent.event;
        QParticipationRequest participation = QParticipationRequest.participationRequest;

        // Строим запрос
        JPAQuery<Tuple> query = new JPAQuery<>(em)
                .select(event, participation.count())
                .from(event)
                .leftJoin(participation).on(participation.event.id.eq(event.id)
                        .and(participation.status.eq(status)))
                .where(eventCondition)
                .groupBy(event.id);

        // Выполняем запрос
        List<Tuple> results = query.fetch();

        // обработка результата
        List<Event> events = new ArrayList<>();
        for (Tuple tuple : results) {
            Event e = tuple.get(event);  // Извлекаем событие
            if (e != null) {
                Integer confirmedCount = tuple.get(1, Integer.class);  // Извлекаем количество участников
                e.setConfirmedRequests((confirmedCount == null) ? 0 : confirmedCount);  // пишем в транзиентное поле
                if (!onlyAvailable ||
                        e.getParticipantLimit() == 0 ||
                        e.getParticipantLimit() > e.getConfirmedRequests()) {
                    events.add(e);
                }
            }
        }

        int toIndex = Math.min(from + size, events.size());
        if (from >= events.size()) {
            return List.of();
        }
        return events.subList(from, toIndex);
    }
}
