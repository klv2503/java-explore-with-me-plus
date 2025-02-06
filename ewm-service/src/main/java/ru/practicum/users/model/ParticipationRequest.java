package ru.practicum.users.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;

@Entity
@Table(name = "participation_requests",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "event_id"}))
@Getter
@Setter
@NoArgsConstructor
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Column(nullable = false)
    private LocalDateTime created;
}
