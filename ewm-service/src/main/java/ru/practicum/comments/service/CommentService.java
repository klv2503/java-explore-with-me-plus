package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentOutDto;

public interface CommentService {
    CommentOutDto addNewComment(Long userId, Long eventId, CommentDto commentDto);

}
