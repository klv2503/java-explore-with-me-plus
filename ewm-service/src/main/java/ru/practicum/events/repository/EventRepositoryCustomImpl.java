package ru.practicum.events.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.QEvent;

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
}
