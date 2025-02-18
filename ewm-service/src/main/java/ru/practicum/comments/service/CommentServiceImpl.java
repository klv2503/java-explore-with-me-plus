package ru.practicum.comments.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentOutDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.errors.EventNotPublishedException;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.StateEvent;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.service.AdminUserService;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private EventRepository eventRepository;
    private AdminUserService adminUserService;
    private CommentRepository commentRepository;

    @Override
    @Transactional
    public CommentOutDto addNewComment(Long userId, Long eventId, CommentDto commentDto) {
        User user = adminUserService.getUser(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));
        if (!event.getState().equals(StateEvent.CANCELED)) {
            Comment comment = commentRepository.save(CommentMapper.dtoToComment(commentDto, user, event));
            return CommentMapper.commentToOutDto(comment);
        }
        throw new EventNotPublishedException("Status is canceled");
    }
}
