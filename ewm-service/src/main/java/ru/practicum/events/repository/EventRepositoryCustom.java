package ru.practicum.events.repository;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.events.model.Event;

public interface EventRepositoryCustom {
    Page<Event> findAllWithBuilder(BooleanBuilder builder, Pageable pageable);
}