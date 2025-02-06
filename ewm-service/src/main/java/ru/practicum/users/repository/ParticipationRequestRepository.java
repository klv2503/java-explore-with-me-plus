package ru.practicum.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.users.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long>, QuerydslPredicateExecutor<ParticipationRequest> {

    List<ParticipationRequest> findByUserId(Long userId);

    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    Optional<ParticipationRequest> findByIdAndUserId(Long id, Long userId);
}
