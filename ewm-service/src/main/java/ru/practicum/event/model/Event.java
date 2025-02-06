package ru.practicum.event.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.users.model.User;

@Entity
@Table(name = "event")
@Data
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @Column(nullable = false)
    private EventState state;

    @Column(nullable = false)
    private boolean requestModeration;

    @Column(nullable = false)
    private int participantLimit;

    @Column(nullable = false)
    private int confirmedRequests;
}
