package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentOutputDto;
import ru.practicum.comments.dto.CommentPagedDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentsOrder;

public interface CommentService {

    CommentPagedDto getComments(Long eventId, int page, int size, CommentsOrder sort);

    CommentOutputDto addComment(CommentDto commentDto);

    CommentOutputDto updateComment(Long userId, Long commentId, String text);

    Comment getComment(Long id);
}
