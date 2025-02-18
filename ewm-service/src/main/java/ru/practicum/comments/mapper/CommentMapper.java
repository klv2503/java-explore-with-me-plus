package ru.practicum.comments.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentOutDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.model.CommentsStatus;
import ru.practicum.config.DateConfig;
import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;

@Component
public class CommentMapper {

    public static Comment dtoToComment(CommentDto commentDto, User user, Event event) {
        return Comment.builder()
                .id(commentDto.getId())
                .user(user)
                .event(event)
                .text(commentDto.getText())
                .created(LocalDateTime.now())
                .status(CommentsStatus.PUBLISHED)
                .build();
    }

    public static CommentOutDto commentToOutDto(Comment comment) {
        return CommentOutDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getUser().getName())
                .eventName(comment.getEvent().getTitle())
                .created(comment.getCreated().toString())
                .status(comment.getStatus().toString())
                .build();
    }
}
