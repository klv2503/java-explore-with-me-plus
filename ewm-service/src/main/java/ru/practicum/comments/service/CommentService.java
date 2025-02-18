package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentOutputDto;

public interface CommentService {

    CommentOutputDto addComment(CommentDto commentDto);

}
