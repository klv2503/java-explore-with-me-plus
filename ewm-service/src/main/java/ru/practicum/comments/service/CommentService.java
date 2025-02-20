package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentEconomDto;
import ru.practicum.comments.dto.CommentPagedDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentsOrder;

import java.time.LocalDateTime;

public interface CommentService {

    CommentPagedDto getComments(Long eventId, int page, int size, CommentsOrder sort);

    CommentEconomDto addComment(Long userId, CommentDto commentDto);

    CommentEconomDto updateComment(Long userId, Long commentId, String text);

    Comment getComment(Long id);

    void softDelete(Long userId, Long commentId);

    void deleteById(Long commentId);
}
