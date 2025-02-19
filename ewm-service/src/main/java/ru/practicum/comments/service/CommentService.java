package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentOutDto;
import ru.practicum.comments.dto.CommentPagedDto;
import ru.practicum.comments.model.CommentsOrder;

public interface CommentService {

    CommentPagedDto getComments(Long eventId, int page, int size, CommentsOrder sort);

    CommentOutDto addNewComment(Long userId, Long eventId, CommentDto commentDto);

}
