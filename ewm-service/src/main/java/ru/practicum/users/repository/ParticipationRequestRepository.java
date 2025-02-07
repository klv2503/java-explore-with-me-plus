package ru.practicum.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import ru.practicum.users.model.ParticipationRequest;
import ru.practicum.users.model.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long>, QuerydslPredicateExecutor<ParticipationRequest> {

    List<ParticipationRequest> findByUserId(Long userId);

    boolean existsByUserIdAndEventId(Long userId, Long eventId);

    Optional<ParticipationRequest> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT COUNT(pr) FROM ParticipationRequest pr WHERE pr.status = :status AND pr.event.id = :eventId")
    long countConfirmedRequestsByStatusAndEventId(@Param("status") RequestStatus status, @Param("eventId") Long eventId);
}
